package edu.miu.webstorebackend.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductDto {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String imageUrl;
    private double price;
    private double discount;
    private int quantity;
}
