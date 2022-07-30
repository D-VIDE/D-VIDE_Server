package com.divide.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class getNearByPostsRequest {
    private double longitude; //경도: x
    private double latitude; //위도: y
}
