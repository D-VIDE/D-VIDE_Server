package com.divide.post.domain;

import com.divide.order.Order;
import com.divide.user.User;
import lombok.*;
import org.locationtech.jts.geom.Point;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.Nullable;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@Table(name = "POST")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") //FK
    @Nullable
    private User user;

    @NotEmpty
    private String title;
    @NotEmpty
    private String storeName;
    @NotNull
    private String content;

    @PositiveOrZero
    private int targetPrice;
    @PositiveOrZero
    private int deliveryPrice;

    @PositiveOrZero
    private int orderedPrice;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Category category;

    @FutureOrPresent
    private LocalDateTime targetTime;
    @NotNull
    private Point deliveryLocation;
    @Enumerated(EnumType.STRING)
    @NotNull
    private PostStatus postStatus;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL)
    @NotNull
    private List<PostImage> postImages = new ArrayList();

    @CreatedDate
    private LocalDateTime createdAt;

    //==생성 메서드==
    @Builder
    private Post(User user, String title, String storeName, String content, int targetPrice, int deliveryPrice, Category category, LocalDateTime targetTime, Point deliveryLocation, PostStatus postStatus, List<String> postImgUrls) {
        this.user = user;
        this.title = title;
        this.storeName = storeName;
        this.content = content;
        this.targetPrice = targetPrice;
        this.deliveryPrice = deliveryPrice;
        this.category = category;
        this.targetTime = targetTime;
        this.deliveryLocation = deliveryLocation;
        this.postStatus = postStatus;
        this.postImages = postImgUrls.stream().map(postImgUrl -> new PostImage(this, postImgUrl)).toList();
    }


    //PostService 오류 삭제 하기 위한 임시 update메서드
    public void updateInfo(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addOrder(Order order) {
        this.orderedPrice += order.getOrderPrice();
    }

    public PostStatus checkStatus() {
        /**
         * 시간이 지나기 전일 때 => 항상 RECRUITING
         * 시간이 지났을 때 => 돈이 모였으면? RECRUIT_SUCCESS
         *                  돈이 안 모였으면? RECRUIT_FAIL
         */
        LocalDateTime now = LocalDateTime.now();
        Boolean timeOver = targetTime.isBefore(now);
        Boolean moneyOver = targetPrice < orderedPrice;
        if (!timeOver) {
            return PostStatus.RECRUITING;
        } else if (moneyOver) {
            return PostStatus.RECRUIT_SUCCESS;
        } else {
            return PostStatus.RECRUIT_FAIL;
        }
    }
}
