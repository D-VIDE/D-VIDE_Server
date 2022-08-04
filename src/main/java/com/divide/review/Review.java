package com.divide.review;

import com.divide.post.domain.Post;
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
    @NotNull
    private User user;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id") //FK
    @NotNull
    private Post post;

    @NotEmpty
    private Double starRating;
    @NotEmpty
    private Geometry storeLocation;

    @CreatedDate
    private LocalDateTime createdAt;

    @NotNull
    private String content;

    @Builder
    public Review(Long reviewId, User user, Post post, Double starRating, Geometry storeLocation, LocalDateTime createdAt, String content) {
        this.reviewId = reviewId;
        this.user = user;
        this.post = post;
        this.starRating = starRating;
        this.storeLocation = storeLocation;
        this.createdAt = createdAt;
        this.content = content;
    }
}
