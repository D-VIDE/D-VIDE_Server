package com.divide.order.dto.response;

import com.divide.common.CommonPostResponse;
import com.divide.common.CommonUserResponse;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetOrdersResponseV2 {
    private CommonUserResponse user;
    private PostResponseWithStoreName post;
    private OrderResponse order;

    @Getter
    @AllArgsConstructor
    public static class OrderResponse {
        private int orderedPrice;
        private LocalDateTime orderedTime;
    }
}