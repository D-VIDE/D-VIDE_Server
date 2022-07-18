package com.divide.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostResponse {
    private String title;
    private String content;
    private String message = "게시글이 업데이트 되었습니다.";

    public UpdatePostResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
