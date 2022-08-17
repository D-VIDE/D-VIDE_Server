package com.divide.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class PostReviewRequestV2 {
    @PositiveOrZero
    @NotNull
    private double starRating;

    @NotNull
    private String content;

    @NotNull
    private Long postId;
}