package com.divide.follow.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@Getter
@Setter
public class GetFollowOtherRequest {
    @PositiveOrZero
    private Long userId;

    @PositiveOrZero
    private Integer first = 0;
}
