package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.domain.*;
import edu.miu.webstorebackend.model.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class OrderDto {
    private Long id;
    private Long userId;
    private List<OrderItem> orderItems;
    private OrderStatus status;
    private Address shippingAddress;
    private Address billingAddress;
    private LocalDateTime orderDate;
}
