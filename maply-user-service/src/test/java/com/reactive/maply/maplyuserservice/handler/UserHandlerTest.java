package com.reactive.maply.maplyuserservice.handler;

import com.reactive.maply.maplyuserservice.entity.UserEntity;
import com.reactive.maply.maplyuserservice.repository.UserRepository;
import com.reactive.maply.maplyuserservice.router.RouterConfiguration;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest
@ExtendWith(SpringExtension.class)
@Import({RouterConfiguration.class, UserHandler.class})
class UserHandlerTest {
    @Autowired private WebTestClient webTestClient;
    @MockBean private UserRepository userRepository;
    @Test
    void findOne() {
        final String username = "Maciej";
        UserEntity userEntity = new UserEntity(1L, username,"Maciej",
                "Kwiatkowski", "maciej@gmail.com");
        Mono<UserEntity> userEntityMono = Mono.just(userEntity);
        Mockito
                .when(userRepository.findByUsername(username))
                .thenReturn(userEntityMono);
        webTestClient
                .get()
                .uri("/api/users/{username}", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(1L)
                .jsonPath("$.username").isEqualTo("Maciej")
                .jsonPath("$.firstName").isEqualTo("Maciej")
                .jsonPath("$.lastName").isEqualTo("Kwiatkowski")
                .jsonPath("$.email").isEqualTo("maciej@gmail.com");
    }
}