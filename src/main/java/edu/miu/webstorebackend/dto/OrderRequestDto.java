package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.domain.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderRequestDto {
    private List<OrderItemRequestDto> orderItems;
    private Address shippingAddress;
    private Address billingAddress;
}
