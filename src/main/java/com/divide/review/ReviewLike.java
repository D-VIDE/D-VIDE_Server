package com.divide.review;

import com.divide.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static javax.persistence.FetchType.LAZY;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewLikeId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "review_id")
    @NotNull
    private Review review;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @Builder
    public ReviewLike(Long reviewLikeId, Review review, User user) {
        this.reviewLikeId = reviewLikeId;
        this.review = review;
        this.user = user;
    }
}