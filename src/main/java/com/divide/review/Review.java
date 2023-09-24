package com.divide.review;

import com.divide.post.domain.Post;
import com.divide.post.domain.PostImage;
import com.divide.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;
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
@Table(name = "REVIEW")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id") //FK
    @Nullable
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id") //FK
    @NotNull
    private Post post;


    @PositiveOrZero
    @NotNull
    private Double starRating;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private String content;

    @OneToMany(mappedBy = "review", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private List<ReviewLike> reviewLikes = new ArrayList();

    @OneToMany(mappedBy = "review", orphanRemoval = true, cascade = CascadeType.ALL)
    @NotNull
    private List<ReviewImage> reviewImages = new ArrayList<>();

    //==연관관계 편의메서드==
    public void addReviewImage(ReviewImage reviewImage){
        this.reviewImages.add(reviewImage);
        reviewImage.setReview(this);
    }

    @Builder
    public Review(Long reviewId, User user, Post post, Double starRating, LocalDateTime createdAt, String content, List<ReviewImage> reviewImages) {
        this.reviewId = reviewId;
        this.user = user;
        this.post = post;
        this.starRating = starRating;
        this.createdAt = createdAt;
        this.content = content;
        for(ReviewImage reviewImage: reviewImages){
            this.addReviewImage(reviewImage);
        }
    }
}
