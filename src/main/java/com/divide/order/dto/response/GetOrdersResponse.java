package com.divide.order.dto.response;

import com.divide.post.domain.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class GetOrdersResponse {

    @Getter
    @AllArgsConstructor
    public static class Poster{
        private Long id;
        private String nickname;
        private String profileImgUrl;
    }
    private Poster poster;
    private Double longitude;
    private Double latitude;
    private Long postId;
    private String title;
    private LocalDateTime targetTime;
    private Integer targetPrice;
    private Integer orderedPrice;
    private PostStatus status;
    private String postImgUrl;
}