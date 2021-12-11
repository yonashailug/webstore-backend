package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@Entity
@NoArgsConstructor
public class ShoppingCart {
    @Id
    private Long id;

    @OneToOne
    private Buyer buyer;

    @OneToMany
    private List<Product> products;

}
