package com.divide.review.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
public class PostReviewRequest {
    @PositiveOrZero
    @NotNull
    private double starRating;

    @NotNull
    private String content;

    @NotEmpty
    private String storeName;
}
