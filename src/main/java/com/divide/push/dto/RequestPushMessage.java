package com.divide.push.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "푸쉬 메시지")
public class RequestPushMessage {
    String title;
    String body;

    @Builder
    public RequestPushMessage(String title, String body, String image) {
        this.title = title;
        this.body = body;
    }
}
