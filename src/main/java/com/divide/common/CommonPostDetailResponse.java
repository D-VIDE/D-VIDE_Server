package com.divide.common;

import com.divide.post.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@AllArgsConstructor
public class CommonPostDetailResponse {
    private Long id;
    private Double longitude;
    private Double latitude;
    private String title;
    private LocalDateTime targetTime;
    private Integer targetPrice;
    private Integer deliveryPrice;
    private Integer orderedPrice;
    private String content;
    private String storeName;
    private List<String> postImgUrls;
}
