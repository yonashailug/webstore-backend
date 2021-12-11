package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
@NoArgsConstructor
public class OrderItem {
    @Id
    private Long id;

    @ManyToOne
    private Product product;

    private int quantity;

    @ManyToOne
    private Order order;

    private OrderStatus status;
}
