package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class Runner implements CommandLineRunner {

    private Resource resource;
    private String url;
    private WebClient webClient;

    public Runner(@Value("${target.application.route}") String url,
                  @Value("classpath:routes.json") Resource resource) {
        this.url = url;
        this.webClient = WebClient.create(url);
        this.resource = resource;
    }

    private static class Coordinates {

        private Double x;
        private Double y;
        private String username;

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public String getUsername() {
            return username;
        }

        @Override
        public String toString() {
            return String.format("username: %s, x: %s, y: %s", this.username, this.x, this.y);
        }
    }

    @Override
    public void run(String... args) throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resource.getFile());
        ObjectReader objectReader = objectMapper.readerFor(new TypeReference<Map<String, List<Coordinates>>>() {
        });
        Map<String, List<Coordinates>> map = objectReader.readValue(jsonNode);

        List<Flux<Coordinates>> fluxList = map.values().stream().map(Flux::fromIterable).collect(Collectors.toList());

        for(Flux<Coordinates> coordinatesFlux: fluxList) {
            Thread.sleep(1000);
            Flux
                    .interval(Duration.ofSeconds(5))
                    .zipWith(coordinatesFlux, (tick, coordinate) -> {
                        System.out.println(String.format("Username: %s, {lat = %s, lng = %s}", coordinate.getUsername(), coordinate.getX(), coordinate.getY()));
                        return webClient
                                .post()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(String.format("{\"username\": \"%s\", \"lat\": %s, \"lng\": %s}", coordinate.username, coordinate.getX(), coordinate.getY()))
                                .exchange();
                    })
                    .flatMap(clientResponseMono -> clientResponseMono)
                    .repeat(15) // repeats stream n + 1 times
                    .doOnComplete(() -> System.out.println("Flux completed"))
                    .subscribe();
        }
    }

}
