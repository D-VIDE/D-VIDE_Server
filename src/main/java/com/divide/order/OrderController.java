package com.divide.order;

import com.divide.order.dto.request.GetOrdersRequest;
import com.divide.order.dto.request.PostOrderRequest;
import com.divide.order.dto.response.GetOrdersResponse;
import com.divide.order.dto.response.PostOrderResponse;
import com.divide.post.dto.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/v1/orders")
    public ResponseEntity<Result<List<GetOrdersResponse>>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid GetOrdersRequest getOrdersRequest
    ) {
        List<GetOrdersResponse> orders = orderService.findOrders(userDetails.getUsername(), getOrdersRequest.getFirst());
        return ResponseEntity.ok(new Result<>(orders));
    }

    @PostMapping("/v1/order")
    public ResponseEntity<PostOrderResponse> postOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute PostOrderRequest postOrderRequest
    ) {
        Long orderId = orderService.saveOrder(userDetails.getUsername(), postOrderRequest.getPostId(), postOrderRequest.getOrderPrice(), postOrderRequest.getOrderImg());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostOrderResponse(orderId));
    }
}
