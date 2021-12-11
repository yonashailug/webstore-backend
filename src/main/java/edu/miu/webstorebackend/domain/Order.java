package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

enum OrderStatus {
    ORDERED, ACCEPTED, SHIPPED, DELIVERED, CANCELED;
}

@Entity
@NoArgsConstructor
public class Order {
    @Id
    private Long id;

    @ManyToOne
    private Buyer buyer;

    @OneToMany
    private List<OrderItem> orderItems;

    private OrderStatus status;

    @OneToMany
    private List<Coupon> appliedCoupons;

    @OneToOne
    private Address shippingAddress;
    private LocalDateTime orderDate;

    @OneToOne
    private Transaction transaction;
}
