package com.divide.post.dto.response;

import com.divide.post.Category;
import com.divide.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class getNearByPostsResponse {
    private Long postId;

    private String profileImgUrl;
    private String nickname;

    private double longitude;
    private double latitude;

    private LocalDateTime targetTime;
    private String title;
    private int targetPrice;
    private Category category;

    public getNearByPostsResponse(Post p) {
        this.postId = p.getPostId();
        this.nickname = p.getUser().getNickname();
        this.profileImgUrl = p.getUser().getProfileImgUrl();
        this.longitude = p.getDeliveryLocation().getCoordinate().getX();
        this.latitude = p.getDeliveryLocation().getCoordinate().getY();
        this.targetTime = p.getTargetTime();
        this.title = p.getTitle();
        this.targetPrice = p.getTargetPrice();
        this.category = p.getCategory();
    }
}
