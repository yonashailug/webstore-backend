package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@NoArgsConstructor
public class Buyer extends Role {
    @Id
    private Long id;

    @OneToOne
    private ShoppingCart cart;

    @OneToMany
    private List<Order> orders;

    @OneToMany
    private List<Seller> following;

    @OneToMany
    private List<Payment> paymentMethods;

    @OneToMany
    private List<Address> shippingAddresses;
}
