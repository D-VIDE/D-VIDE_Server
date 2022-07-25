package com.divide.post;

import com.divide.user.User;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
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

//    @Embedded
//    private Point deliveryLocation;

////    private List<String> orders = new ArrayList<>();
////    private List<Order> orders = new ArrayList();
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @CreatedDate
    private LocalDateTime createdAt;

//==연관관계 편의메서드==
    public void setUser(User user){
        this.user = user;
        user.getPosts().add(this);
    }

    //==생성 메서드==

    @Builder
    public Post(User user, String title, String storeName, String content, int targetPrice, int deliveryPrice, int targetUserCount, Category category, LocalDateTime targetTime, Point deliveryLocation, PostStatus postStatus) {
        this.user = user;
        this.title = title;
        this.storeName = storeName;
        this.content = content;
        this.targetPrice = targetPrice;
        this.deliveryPrice = deliveryPrice;
        this.targetUserCount = targetUserCount;
        this.category = category;
        this.targetTime = targetTime;
//        this.deliveryLocation = deliveryLocation;
        this.postStatus = postStatus;
    }

    //PostService 오류 삭제 하기 위한 임시 update메서드
    public void updateInfo(String title, String content){
        this.title = title;
        this.content = content;
    }
}
