package com.divide.follow.dto.response;

import com.divide.follow.FollowRelation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;


@Getter
@Setter
@NoArgsConstructor
public class GetFollowRequest {
    @NotNull
    private FollowRelation relation;

    @PositiveOrZero
    private Integer first = 0;
}
