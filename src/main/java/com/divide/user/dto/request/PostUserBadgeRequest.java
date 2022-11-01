package com.divide.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Getter
@NoArgsConstructor
public class PostUserBadgeRequest {
    @NotEmpty
    private String badgeName;
}
