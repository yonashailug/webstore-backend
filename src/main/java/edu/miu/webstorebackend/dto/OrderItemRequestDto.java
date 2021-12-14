package edu.miu.webstorebackend.dto;

import lombok.Data;

@Data
public class OrderItemRequestDto {
    private Long productId;
    private int quantity;
}
