package com.reactive.maply.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PositionEntity {
    private String username;
    private Double lat;
    private Double lng;
}
