package com.reactive.maply.repository;

import com.reactive.maply.model.Request;
import org.springframework.data.domain.Range;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.connection.stream.ObjectRecord;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStreamOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Repository
public class PositionRepository {

    private ReactiveStreamOperations<String, String, String> reactiveStreamOperations;

    public PositionRepository(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveStreamOperations = reactiveRedisTemplate.opsForStream();
    }

    public Flux<ObjectRecord<String, Request>> range(String streamKey,
                                                     Optional<String> from,
                                                     Optional<String> to,
                                                     Optional<String> username) {
        RecordId left = from.map(RecordId::of).orElse(RecordId.of("0-0"));
        RecordId right = to.map(RecordId::of).orElse(RecordId.of(System.currentTimeMillis(), 0));
        Range range = Range.from(Range.Bound.inclusive(left.getValue())).to(Range.Bound.inclusive(right.getValue()));
        RedisZSetCommands.Limit limit = RedisZSetCommands.Limit.limit().count(20);
        Flux<ObjectRecord<String, Request>> returnFlux = reactiveStreamOperations.reverseRange(Request.class, streamKey, range, limit);
        if (username.isPresent()) {
            returnFlux = returnFlux.filter(or -> or.getValue().getUsername().equals(username.get()));
        }
        return returnFlux;
    }

    public Mono<RecordId> add(String streamKey, Request request) {
        return reactiveStreamOperations.add(ObjectRecord.create(streamKey, request));
    }

}
