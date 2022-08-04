package com.divide.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Getter
@NoArgsConstructor
public class GetNearbyPostsRequest {
    @NotNull
    private double longitude; //경도: x

    @NotNull
    private double latitude; //위도: y
}
