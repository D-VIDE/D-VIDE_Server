package com.divide.post.dto.request;

import com.divide.post.Category;
import com.divide.post.PostStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.geom.Point;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Embedded;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern= "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime targetTime;

    private String deliveryLocation; //(Point) new WKTReader().read(deliveryLocation);로 변환해야함

    private PostStatus postStatus;
}
