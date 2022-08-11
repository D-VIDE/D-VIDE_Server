package com.divide.order;

import com.divide.order.dto.request.GetOrdersRequest;
import com.divide.order.dto.request.PostOrderRequest;
import com.divide.order.dto.response.GetOrdersResponse;
import com.divide.order.dto.response.PostOrderResponse;
import com.divide.post.PostService;
import com.divide.post.domain.Post;
import com.divide.post.dto.response.Result;
import com.divide.user.User;
import com.divide.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final PostService postService;
    @PostMapping("/v1/order")
    public ResponseEntity<PostOrderResponse> postOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute PostOrderRequest postOrderRequest
    ) {
        System.out.println("userDetails = " + userDetails + ", postOrderRequest = " + postOrderRequest);
        User user = userService.getUserByEmail(userDetails.getUsername());
        Post post = postService.findOne(postOrderRequest.getPostId());
        Long orderId = orderService.saveOrder(user, post, postOrderRequest.getOrderPrice(), postOrderRequest.getOrderImg());
        return ResponseEntity.status(HttpStatus.CREATED).body(new PostOrderResponse(orderId));
    }
}
