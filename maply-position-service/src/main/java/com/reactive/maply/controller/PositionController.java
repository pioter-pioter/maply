package com.reactive.maply.controller;

import com.reactive.maply.model.Request;
import com.reactive.maply.repository.PositionRepository;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping(value = "/api/position/{streamKey}")
@CrossOrigin(origins = "*")
public class PositionController {

    private PositionRepository positionRepository;

    public PositionController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @GetMapping
    public Flux<ObjectRecord<String, Request>> range(@PathVariable String streamKey,
                                                     @RequestParam Optional<String> from,
                                                     @RequestParam Optional<String> to,
                                                     @RequestParam Optional<String> username) {
        return positionRepository.range(streamKey, from, to, username);
    }

    @PostMapping
    public Mono<RecordId> publish(@PathVariable String streamKey, @RequestBody Request request) {
        return positionRepository.add(streamKey, request);
    }

}
