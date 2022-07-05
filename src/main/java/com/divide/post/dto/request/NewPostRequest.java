package com.divide.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewPostRequest {
    private String title;
    private String content;
}
