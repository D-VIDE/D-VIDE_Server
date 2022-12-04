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
import org.springframework.util.StringUtils;
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
    public Long saveOrder(String userEmail, Long postId, Integer orderPrice, List<MultipartFile> orderImgFileList) {
        // 엔티티 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        Post post = postRepository.findByPostId(postId);

        // validation
        if (post.getPostStatus() != PostStatus.RECRUITING) {
            throw new RestApiException(PostErrorCode.POST_NOT_RECRUITING);
        }

        // 주문 이미지 생성
        List<String> orderImgUrls = orderImgFileList.stream().map(orderImgFile -> {
            String extension = StringUtils.getFilenameExtension(orderImgFile.getOriginalFilename()).toLowerCase();
            return OCIUtil.uploadFile(orderImgFile, OCIUtil.FolderName.ORDER, postId + "/" + UUID.randomUUID() + "." + extension);
        }).toList();
        Order order = new Order(user, post, orderPrice, orderImgUrls);
        orderRepository.save(order);

        return order.getId();
    }

    @Transactional(readOnly = true)
    public Boolean checkOrdered(String userEmail, Long postId) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        List<Order> orders = orderRepository.findOrdersByPostId(postId);

        return orders.stream().anyMatch(order -> order.getUser().equals(user));
    }

    @Transactional
    public Long saveOrderTest(String userEmail, Long postId, Integer orderPrice, List<String> orderImgUrlList) {
        // 엔티티 조회
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new UsernameNotFoundException(""));
        Post post = postRepository.findByPostId(postId);

        // validation
        if (post.checkStatus() != PostStatus.RECRUITING) {
            throw new RestApiException(PostErrorCode.POST_NOT_RECRUITING);
        }

        // 주문 이미지 생성
        Order order = new Order(user, post, orderPrice, orderImgUrlList);
        orderRepository.save(order);

        return order.getId();
    }
}
