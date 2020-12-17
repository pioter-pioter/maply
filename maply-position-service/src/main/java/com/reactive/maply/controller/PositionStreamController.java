package com.reactive.maply.controller;

import com.reactive.maply.model.Request;
import com.reactive.maply.service.PositionStreamService;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@CrossOrigin(origins = "*")
public class PositionStreamController {

    private PositionStreamService positionStreamService;

    public PositionStreamController(PositionStreamService positionStreamService) {
        this.positionStreamService = positionStreamService;
    }

    @GetMapping(path="/sse/position/{streamKey}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ObjectRecord<String, Request>> subscribe(@PathVariable String streamKey) {
        return positionStreamService.subscribe(streamKey);
    }

}
