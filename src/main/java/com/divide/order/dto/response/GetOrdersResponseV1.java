package com.divide.order.dto.response;

import com.divide.common.CommonPostResponse;
import com.divide.common.CommonUserResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Deprecated
@Getter
@AllArgsConstructor
public class GetOrdersResponseV1 {
    private CommonUserResponse user;
    private CommonPostResponse post;
    private OrderResponse order;

    @Getter
    @AllArgsConstructor
    public static class OrderResponse {
        private int orderedPrice;
        private LocalDateTime orderedTime;
    }
}