package com.divide.post.domain;

import lombok.Getter;

@Getter
public class Location {
    private Double latitude;
    private Double longitude;
    public Location(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
