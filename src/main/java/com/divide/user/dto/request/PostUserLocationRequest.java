package com.divide.user.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class PostUserLocationRequest {
    private Double latitude;
    private Double longitude;
}
