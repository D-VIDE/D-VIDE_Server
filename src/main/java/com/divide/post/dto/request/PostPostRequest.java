package com.divide.post.dto.request;

import com.divide.post.domain.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "yyyy-MM-dd HH:mm:ss")
    @NotNull
    private LocalDateTime targetTime;

//    private List<String> postImages
    @NotNull
    private double longitude; //경도: x
    @NotNull
    private double latitude; //위도: y
    @NotNull
    private String content;

}
