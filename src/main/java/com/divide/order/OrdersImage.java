package com.divide.order;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class OrdersImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_image_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orders_id")
    @NotNull
    private Orders orders;

    @NotNull
    private String url;
}
