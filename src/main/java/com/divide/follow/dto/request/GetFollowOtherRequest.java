package com.divide.follow.dto.request;

import com.divide.follow.FollowRelation;
import javax.validation.constraints.NotNull;
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

    @NotNull
    private FollowRelation relation;

    @PositiveOrZero
    private Integer first = 0;
}
