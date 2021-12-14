package edu.miu.webstorebackend.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

import edu.miu.webstorebackend.model.User;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Product {
    @Id
    private Long id;

    @ManyToOne
    private User seller;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
    private double tax;
    private double discount;
    private int stockCount;

    @OneToMany
    private List<Rating> ratings;

    @OneToMany
    private List<Review> reviews;
}
