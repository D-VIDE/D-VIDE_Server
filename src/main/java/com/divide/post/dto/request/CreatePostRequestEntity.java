package com.divide.post.dto.request;

import com.divide.post.Point;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class CreatePostRequestEntity {

    private String title;
//    private String category;
//    //    private Category category;
//
//    private String storeName;
//
//    private int deliveryPrice;
//    private int targetPrice;
//    private int targetUserCount;
//
//    private LocalDateTime targetTime;
    private Point deliveryLocation;

    private String content;
}
