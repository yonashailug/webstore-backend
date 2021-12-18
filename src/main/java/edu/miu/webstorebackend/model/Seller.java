package edu.miu.webstorebackend.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

enum SellerStatus {
    REQUESTED,APPROVED,SUSPENDED,REJECTED
}

@Entity
@NoArgsConstructor
public class Seller {
    @Id
    private Long id;

    private SellerStatus status;

    @OneToMany
    private List<Product> products;

    @OneToMany
    private List<OrderItem> orderItems;
}
