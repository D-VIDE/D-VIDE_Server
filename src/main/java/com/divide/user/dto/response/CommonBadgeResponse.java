package com.divide.user.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonBadgeResponse {
    private String name;
    private String description;
}
