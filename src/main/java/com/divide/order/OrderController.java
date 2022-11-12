package com.divide.order;

import com.divide.fcm.FirebaseCloudMessageService;
import com.divide.order.dto.request.GetOrdersRequest;
import com.divide.order.dto.request.PostOrderRequest;
import com.divide.order.dto.response.GetOrdersResponse;
import com.divide.order.dto.response.PostOrderResponse;
import com.divide.post.PostService;
import com.divide.post.dto.response.Result;
import com.divide.user.User;
import com.divide.user.UserService;
import com.divide.utils.OCIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final PostService postService;
    private final FirebaseCloudMessageService firebaseCloudMessageService;

    @GetMapping("/v1/orders")
    public ResponseEntity<Result<List<GetOrdersResponse>>> getOrders(
            @AuthenticationPrincipal UserDetails userDetails,
            @ModelAttribute GetOrdersRequest getOrdersRequest
    ) {
        List<GetOrdersResponse> orders = orderService.findOrders(userDetails.getUsername(), getOrdersRequest.getFirst());
        return ResponseEntity.ok(new Result<>(orders));
    }

    @PostMapping("/v1/order")
    public ResponseEntity<PostOrderResponse> postOrder(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestPart @Valid PostOrderRequest request,
            @RequestPart @Size(min = 1, max = 3) List<MultipartFile> orderImgFiles
    ) throws IOException {
        Long orderId = orderService.saveOrder(userDetails.getUsername(), request.getPostId(), request.getOrderPrice(), orderImgFiles);

        //주문 생성시 게시글 작성자에게 알림
        //게시글 작성자
        User postUser = postService.findOne(request.getPostId()).getUser();
        //주문 생성자
        User orderUser = userService.getUserByEmail(userDetails.getUsername());
        firebaseCloudMessageService.sendMessageTo(postUser, "주문 알림", orderUser.getNickname() + "님이 주문했습니다");

        return ResponseEntity.status(HttpStatus.CREATED).body(new PostOrderResponse(orderId));
    }
}
