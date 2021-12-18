package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.model.OrderStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemResponseDto implements Serializable {
    private ProductDto product;
    private int quantity;
    private OrderStatus status;
}
