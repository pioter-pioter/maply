package com.reactive.maply.service;

import com.reactive.maply.model.Request;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStreamOperations;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PositionStreamService {

    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    private ReactiveStreamOperations<String, String, String> reactiveStreamOperations;

    public PositionStreamService(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.reactiveStreamOperations = this.reactiveRedisTemplate.opsForStream();
    }

    public Flux<ObjectRecord<String, Request>> subscribe(String streamKey) {
        StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, Request>> options =
                StreamReceiver.StreamReceiverOptions
                        .builder()
                        .targetType(Request.class)
                        .build();
        StreamReceiver<String, ObjectRecord<String, Request>> streamReceiver =
                StreamReceiver.create(
                        reactiveRedisTemplate.getConnectionFactory(),
                        options
                );
        return streamReceiver
                .receive(StreamOffset.latest(streamKey));
    }

}
