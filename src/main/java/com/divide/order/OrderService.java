package com.divide.order;

import com.divide.common.CommonPostResponse;
import com.divide.common.CommonUserResponse;
import com.divide.exception.RestApiException;
import com.divide.exception.code.PostErrorCode;
import com.divide.order.dto.response.GetOrdersResponse;
import com.divide.post.PostRepository;
import com.divide.post.domain.Post;
import com.divide.post.domain.PostStatus;
import com.divide.user.User;
import com.divide.user.UserRepository;
import com.divide.utils.OCIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public List<GetOrdersResponse> findOrders(String userEmail, Integer first) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Order> orders = orderRepository.findOrders(user, first);

        return orders.stream().map(order -> {
            Post post = order.getPost();
            return new GetOrdersResponse(
                    new CommonUserResponse(
                            post.getUser().getId(),
                            post.getUser().getNickname(),
                            post.getUser().getProfileImgUrl()
                    ),
                    new CommonPostResponse(
                            post.getPostId(),
                            post.getDeliveryLocation().getCoordinate().getX(),
                            post.getDeliveryLocation().getCoordinate().getY(),
                            post.getTitle(),
                            post.getTargetTime(),
                            post.getTargetPrice(),
                            post.getOrderedPrice(),
                            post.getPostStatus(),
                            post.getPostImages().get(0).getPostImageUrl()
                    ),
                    new GetOrdersResponse.OrderResponse(
                            order.getOrderPrice(),
                            order.getCreatedAt()
                    )
            );
        }).collect(Collectors.toList());
    }

    @Transactional
    public Long saveOrder(String userEmail, Long postId, Integer orderPrice) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        Post post = postRepository.findByPostId(postId);

        if (post.checkStatus() != PostStatus.RECRUITING) {
            throw new RestApiException(PostErrorCode.POST_NOT_RECRUITING);
        }

        Order order = new Order(user, post, orderPrice);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional
    public Long saveOrderImage(Long orderId, String orderImgUrl) {
        Order order = orderRepository.findById(orderId).orElseThrow();
        OrderImage orderImage = new OrderImage(order, orderImgUrl);
        orderRepository.save(orderImage);
        return orderImage.getId();
    }
}
