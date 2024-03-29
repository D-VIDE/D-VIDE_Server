package com.divide.post.dto.response;

import com.divide.post.domain.Category;
import com.divide.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Deprecated
public class GetPostsResponseV1 {
    private Long postId;

    private String profileImgUrl;
    private String nickname;

    private double longitude;
    private double latitude;

    private LocalDateTime targetTime;
    private String title;
    private String content;
    private int targetPrice;
    private Category category;

    public GetPostsResponseV1(Post p) {
        this.postId = p.getPostId();
        this.nickname = p.getUser().getNickname();
        this.profileImgUrl = p.getUser().getProfileImgUrl();
        this.longitude = p.getDeliveryLocation().getCoordinate().getX();
        this.latitude = p.getDeliveryLocation().getCoordinate().getY();
        this.targetTime = p.getTargetTime();
        this.title = p.getTitle();
        this.content = p.getContent();
        this.targetPrice = p.getTargetPrice();
        this.category = p.getCategory();
    }
}
