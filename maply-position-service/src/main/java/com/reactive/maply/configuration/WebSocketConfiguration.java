package com.reactive.maply.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.reactive.maply.model.Request;
import com.reactive.maply.repository.PositionRepository;
import com.reactive.maply.service.PositionStreamService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Configuration
public class WebSocketConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }


    @Bean
    public WebSocketHandler webSocketHandler(ObjectMapper objectMapper, PositionStreamService positionStreamService, PositionRepository positionRepository) {
        return new WebSocketHandler() {
            @Override
            public Mono<Void> handle(WebSocketSession webSocketSession) {

                List<String> path = Arrays.asList(webSocketSession.getHandshakeInfo().getUri().getPath().split("/"));
                String streamId = path.get(path.size()-1);

                Mono<Void> in = webSocketSession
                        .receive()
                        .doOnNext(webSocketMessage -> {
                            try {
                                Request request = objectMapper.readValue(webSocketMessage.getPayloadAsText(), Request.class);
                                positionRepository.add(streamId, request).subscribe();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            }
                        })
                        .then();

                Flux<WebSocketMessage> outputStream = positionStreamService
                        .subscribe(streamId)
                        .map(mapRecord -> {
                            try {
                                return webSocketSession.textMessage(objectMapper.writeValueAsString(mapRecord));
                            }
                            catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                        });

                Mono<Void> out = webSocketSession.send(outputStream);

                return Mono.zip(in, out).then();
            }
        };
    }

    @Bean
    public HandlerMapping handlerMapping(WebSocketHandler webSocketHandler) {
        Map<String, WebSocketHandler> mapping = Map.of("/ws/position/{streamKey}", webSocketHandler);
        return new SimpleUrlHandlerMapping(mapping, -1);
    }

}
