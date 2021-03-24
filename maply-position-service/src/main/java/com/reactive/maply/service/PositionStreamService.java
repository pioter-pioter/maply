package com.reactive.maply.service;

import com.reactive.maply.model.PositionEntity;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStreamOperations;
import org.springframework.data.redis.stream.StreamReceiver;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class PositionStreamService {

    private ReactiveRedisTemplate<String, String> reactiveRedisTemplate;
    public PositionStreamService(ReactiveRedisTemplate<String, String> reactiveRedisTemplate) {
        this.reactiveRedisTemplate = reactiveRedisTemplate;
    }

    public Flux<ObjectRecord<String, PositionEntity>> listen(String streamKey) {
        StreamReceiver.StreamReceiverOptions<String, ObjectRecord<String, PositionEntity>> options
                = StreamReceiver.StreamReceiverOptions.builder().targetType(PositionEntity.class).build();
        StreamReceiver<String, ObjectRecord<String, PositionEntity>> streamReceiver
                = StreamReceiver.create(reactiveRedisTemplate.getConnectionFactory(), options);
        return streamReceiver
                .receive(StreamOffset.latest(streamKey));
    }

}

