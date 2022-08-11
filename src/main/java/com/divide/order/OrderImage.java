package com.divide.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class OrderImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    @NotNull
    private Order order;

    @NotNull
    private String url;

    public OrderImage(Order order, String url) {
        this.order = order;
        this.url = url;
    }
}
