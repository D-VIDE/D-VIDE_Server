package com.divide.order;

import com.divide.post.Post;
import com.divide.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
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
}
