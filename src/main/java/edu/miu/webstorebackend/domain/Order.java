package edu.miu.webstorebackend.domain;

import edu.miu.webstorebackend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @Id
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany
    private List<OrderItem> orderItems;

    private OrderStatus status;

    @OneToOne
    private Address shippingAddress;

    @OneToOne
    private Address billingAddress;

    private LocalDateTime orderDate;

}
