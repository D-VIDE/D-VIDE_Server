package com.divide.post.dto.response;

import com.divide.post.domain.Category;
import com.divide.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GetPostsResponse {
    private Long postId;
//    -------------user에서 가져옴-------------
    private String profileImgUrl;
    private String nickname;
//    --------------------------------------
    private String title;
    private String content;

//    private Point deliveryLocation; //오류난다 왤까
    private LocalDateTime targetTime;

    private Category category;

    public GetPostsResponse(Post p) {
        this.postId = p.getPostId();
        this.title = p.getTitle();
        this.content = p.getContent();
        this.nickname = p.getUser().getNickname();
        this.profileImgUrl = p.getUser().getProfileImgUrl();

//        this.deliveryLocation = (Point) p.getDeliveryLocation(); //광광 오류 없애줘요
        this.targetTime = p.getTargetTime();
        this.category = p.getCategory();
    }
}
