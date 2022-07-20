package com.divide.post;

import com.divide.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Setter
@Entity
@Table(name = "POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id @GeneratedValue
    private Long postId;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") //FK
    private User user;

    private String title;
    private String storeName;
    private String content;

////    private List<String> postImages = new ArrayList();
////    private List<PostImage> postImages = new ArrayList();
    private int targetPrice;
    private int deliveryPrice;
    private int targetUserCount;

    @Enumerated(EnumType.STRING)
    private Category category;

    private LocalDateTime targetTime;

    @Embedded
    private Point deliveryLocation;

////    private List<String> orders = new ArrayList<>();
////    private List<Order> orders = new ArrayList();
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    private LocalDateTime createdAt;

//==연관관계 편의메서드==
    public void setUser(User user){
        this.user = user;
        user.getPosts().add(this);
    }

    //==생성 메서드==
    //User, title, content, deliveryLocation
    public static Post createPost(User user, String title, String content, Point deliveryLocation ){
        Post post = new Post();
        post.setUser(user);

        post.setTitle(title);
        post.setContent(content);
        post.setDeliveryLocation(deliveryLocation);

        return post;
    }
}
