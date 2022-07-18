package com.divide.post.dto.response;

import com.divide.post.Post;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class postDto {
    private Long postId;
    private String title;
    private String content;

    public postDto(Post p) {
        this.postId = p.getPostId();
        this.title = p.getTitle();
        this.content = p.getContent();
    }
}
