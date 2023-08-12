package com.divide.order.dto.response;

import com.divide.common.CommonPostResponse;
import com.divide.post.domain.PostStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class PostResponseWithStoreName extends CommonPostResponse {
    private final String storeName;

    public PostResponseWithStoreName(Long id, Double longitude, Double latitude, String title, LocalDateTime targetTime,
                              Integer targetPrice, Integer orderedPrice, PostStatus status, String postImgUrl, String storeName) {
        super(id, longitude, latitude, title, targetTime, targetPrice, orderedPrice, status, postImgUrl);
        this.storeName = storeName;
    }
}
