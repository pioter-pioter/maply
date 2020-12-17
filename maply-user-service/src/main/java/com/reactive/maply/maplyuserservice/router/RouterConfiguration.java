package com.reactive.maply.maplyuserservice.router;

import com.reactive.maply.maplyuserservice.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouterConfiguration {

    @Bean
    public RouterFunction<ServerResponse> routes(UserHandler userHandler) {
        return RouterFunctions.route().path(
                "/api/users",
                builder -> builder
                        .GET("/{username}", userHandler::findOne)
                        .GET(userHandler::findAll)
        ).build();
    }

}
