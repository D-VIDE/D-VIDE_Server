package com.divide.common;

import com.divide.post.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class CommonPostResponse {
    private Long id;
    private Double longitude;
    private Double latitude;
    private String title;
    private LocalDateTime targetTime;
    private Integer targetPrice;
    private Integer orderedPrice;
    private PostStatus status;
    private String postImgUrl;
}
