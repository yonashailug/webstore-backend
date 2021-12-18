package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.domain.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductWithReviewDto {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
    private double discount;
    private int quantity;
    private List<Review> reviews;
}
