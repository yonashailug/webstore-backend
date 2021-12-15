package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.domain.Order;
import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.domain.Product;
import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemResponseDto implements Serializable {
    private ProductDto product;
    private int quantity;
    private OrderStatus status;
}
