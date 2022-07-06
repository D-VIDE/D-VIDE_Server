package com.divide.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String userId;
// private User user;
    private String postId;
// private Post post;
    private int orderPrice;
    private String orderStatus;
//    private OrderStatus orderStatus;
}
