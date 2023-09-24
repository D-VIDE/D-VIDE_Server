package com.divide.review;

import com.divide.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import org.springframework.lang.Nullable;

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
    @Nullable
    private User user;

    @Builder
    public ReviewLike( Review review, User user) {
        this.review = review;
        this.user = user;
    }
}