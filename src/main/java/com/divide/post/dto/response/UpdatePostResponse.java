package com.divide.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePostResponse {
    private String title; //TODO: 응답값 변경
    private String content;

    public UpdatePostResponse(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
