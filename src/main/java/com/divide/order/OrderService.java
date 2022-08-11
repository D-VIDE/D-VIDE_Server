package com.divide.order;

import com.divide.post.domain.Post;
import com.divide.user.User;
import com.divide.utils.OCIUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    @Transactional
    public Long saveOrder(User user, Post post, Integer orderPrice, List<MultipartFile> images) {
        Order order = new Order(user, post, orderPrice);
        orderRepository.save(order);
        images.stream().forEach(multipartFile -> {
            String url = OCIUtil.uploadFile(multipartFile, OCIUtil.FolderName.ORDER, order.getId() + "/" + UUID.randomUUID());
            OrderImage orderImage = new OrderImage(order, url);
            orderRepository.save(orderImage);
        });
        return order.getId();
    }
}
