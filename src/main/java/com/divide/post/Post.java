package com.divide.post;

import com.divide.post.dto.request.CreatePostRequest;
import com.divide.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue
    private Long postId;
//    private String userId;
    private String title;
//    private String storeName;
    private String content;

////    private List<String> postImages = new ArrayList();
////    private List<PostImage> postImages = new ArrayList();
//    private int targetPrice;
//    private int deliveryPrice;
//    private int targetUserCount;
//    private String category;
////    private Category category;
//    private LocalDateTime targetTime;
//    private String location;
////    private List<String> orders = new ArrayList<>();
////    private List<Order> orders = new ArrayList();
//    private String postStatus;
////    private PostStatus postStatus;
//    private LocalDateTime createdAt;
}
