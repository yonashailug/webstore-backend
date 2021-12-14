package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
public class Product {
    @Id
    private Long id;

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
