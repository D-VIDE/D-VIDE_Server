package com.divide.post.dto.request;

import com.divide.post.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class postPostRequest {

    private String title;
    private String storeName;

    private Category category;
    private int deliveryPrice;
    private int targetPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime targetTime;

//    private List<String> postImages

    private double longitude; //경도: x
    private double latitude; //위도: y
    private String content;

}
