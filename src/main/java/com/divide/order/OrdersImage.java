package com.divide.order;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@NoArgsConstructor
public class OrdersImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull
    private Order order;

    @NotNull
    private String url;
}
