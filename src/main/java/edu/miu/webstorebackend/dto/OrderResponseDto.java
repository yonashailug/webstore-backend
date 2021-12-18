package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.model.Address;
import edu.miu.webstorebackend.model.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponseDto {
    private Long id;
    private OrderStatus status;
    private List<OrderItemResponseDto> orderItems;
    private Address shippingAddress;
    private Address billingAddress;
    private LocalDateTime orderDate;
}
