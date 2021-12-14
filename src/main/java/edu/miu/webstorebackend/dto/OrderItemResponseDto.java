package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.domain.Order;
import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.domain.Product;
import lombok.Data;

@Data
public class OrderItemResponseDto {
    private Product product;
    private int quantity;
    private OrderStatus status;
}
