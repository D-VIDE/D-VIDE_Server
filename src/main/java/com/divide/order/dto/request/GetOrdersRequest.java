package com.divide.order.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nullable;
import javax.validation.constraints.PositiveOrZero;

@NoArgsConstructor
@Getter
public class GetOrdersRequest {
    @Nullable
    @PositiveOrZero
    private Integer first = 0;
}
