package com.divide.post.dto.request;

import com.divide.post.Category;
import com.divide.post.Point;
import com.divide.post.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class CreatePostRequest {

    private String title;
    private String storeName;
    private String content;

    private int targetPrice;
    private int deliveryPrice;
    private int targetUserCount;

    private Category category;
    private LocalDateTime targetTime;
    private Point deliveryLocation;

    private PostStatus postStatus;
}
