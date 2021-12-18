package edu.miu.webstorebackend.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Transaction {
    @Id
    private Long id;

    @OneToOne
    private Order order;

    @ManyToOne
    private Payment paymentMethod;

    @OneToOne
    private Buyer buyer;

    private LocalDateTime createdAt;
    private double totalAmount;
    private double shippingCost;
    private double tax;
}
