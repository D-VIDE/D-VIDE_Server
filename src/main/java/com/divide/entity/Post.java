package com.divide.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    private String userId;
//private User user;
    private String title;
    private String content;
    private List<String> postImages = new ArrayList();
//    private List<PostImage> postImages = new ArrayList();
    private int targetPrice;
    private int deliveryPrice;
    private int targetUserCount;
    private String category;
//    private Category category;
    private LocalDateTime targetTime;
    private String location;
    private List<String> orders = new ArrayList<>();
//    private List<Order> orders = new ArrayList();
    private String postStatus;
//    private PostStatus postStatus;
    private LocalDateTime createdAt;
}