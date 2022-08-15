package com.divide.post.dto.request;

import com.divide.post.domain.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;


@Getter
@NoArgsConstructor
public class PostPostRequest {
    @NotEmpty
    private String title;
    @NotEmpty
    private String storeName;

    @NotNull
    private Category category;

    @PositiveOrZero
    @NotNull
    private int deliveryPrice;
    @PositiveOrZero
    @NotNull
    private int targetPrice;

    @NotNull
    private LocalDateTime targetTime;

    @NotNull
    private double longitude; //경도: x
    @NotNull
    private double latitude; //위도: y
    @NotNull
    private String content;
}
