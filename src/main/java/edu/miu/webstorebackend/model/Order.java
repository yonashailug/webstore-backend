package edu.miu.webstorebackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<OrderItem> orderItems;

    private OrderStatus status;

    @OneToOne(cascade = CascadeType.ALL)
    private Address shippingAddress;

    @OneToOne(cascade = CascadeType.ALL)
    private Address billingAddress;

    private LocalDateTime orderDate;

}
