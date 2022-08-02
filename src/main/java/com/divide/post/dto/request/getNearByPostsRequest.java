package com.divide.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class getNearByPostsRequest {
    private double longitude; //경도: x
    private double latitude; //위도: y
}
