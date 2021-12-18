package edu.miu.webstorebackend.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemRequestDto implements Serializable {
    private Long productId;
    private int quantity;
}
