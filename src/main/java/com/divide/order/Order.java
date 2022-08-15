package com.divide.order;

import com.divide.post.domain.Post;
import com.divide.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    @NotNull
    private Post post;

    @NotNull
    private Integer orderPrice;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus orderStatus;
    public enum OrderStatus {
        ACTIVE, CANCEL
    }

    @CreatedDate
    private LocalDateTime createdAt;

    public Order(User user, Post post, Integer orderPrice, List<String> orderImgUrls) {
        this.user = user;
        this.post = post;
        this.orderPrice = orderPrice;
        this.orderStatus = OrderStatus.ACTIVE;
        post.addOrder(this);
        orderImgUrls.forEach(url -> new OrderImage(this, url));
    }
}
