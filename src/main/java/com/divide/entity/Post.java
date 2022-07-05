package com.divide.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "POST")
public class Post {

    @Id @GeneratedValue
    private Long postIdx;
    private String title;
    private String content;

//    private String userId;
//    //private User user;
//
//    private List<String> postImages = new ArrayList();
////    private List<PostImage> postImages = new ArrayList();
//    private int targetPrice;
//    private int deliveryPrice;
//    private int targetUserCount;
//    private String category;
////    private Category category;
//    private LocalDateTime targetTime;
//    private String location;
//    private List<String> orders = new ArrayList<>();
////    private List<Order> orders = new ArrayList();
//    private String postStatus;
////    private PostStatus postStatus;
//    private LocalDateTime createdAt;
}
