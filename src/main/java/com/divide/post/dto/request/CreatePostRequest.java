package com.divide.post.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CreatePostRequest {
//    private String userId;
    //private User user;

    private String title;
//    private String category;
//    //    private Category category;
//
//    private String storeName;
//
//    private int deliveryPrice;
//    private int targetPrice;
//    private int targetUserCount;
//
//    private LocalDateTime targetTime;
//    private String location;

    private String content;
}
