package com.reactive.maply.maplyuserservice.handler;

import com.reactive.maply.maplyuserservice.entity.UserEntity;
import com.reactive.maply.maplyuserservice.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.server.ServerResponse.*;

@Component
public class UserHandler {
    private UserRepository userRepository;
    public UserHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    public Mono<ServerResponse> findOne(ServerRequest request) {
        String username = request.pathVariable("username");
        return userRepository
                .findByUsername(username)
                .flatMap(user -> ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(user))
                .switchIfEmpty(notFound().build());
    }
    public Mono<ServerResponse> findAll(ServerRequest request) {
        return ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(userRepository.findAll(), UserEntity.class);
    }
}

