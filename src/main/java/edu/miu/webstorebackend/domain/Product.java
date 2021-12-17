package edu.miu.webstorebackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

import edu.miu.webstorebackend.model.User;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Product implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private User seller;

    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
    private double tax;
    private double discount;
    private int stockCount;
    private int quantity;

    @OneToMany
    private List<Rating> ratings;

    @OneToMany
    private List<Review> reviews;
}
