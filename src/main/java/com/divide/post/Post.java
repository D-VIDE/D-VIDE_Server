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
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") //FK
    private User user;
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
//==연관관계 편의메서드==
    public void setUser(User user){
        this.user = user;
        user.getPosts().add(this);
    }

    //==생성 메서드==
    public static Post createPost(User user ){
        Post post = new Post();
        post.setUser(user);

        //임시로 제목, 내용 적어놓음
        post.setTitle("제목이에영");
        post.setContent("내용입니당");

        return post;
    }
}
