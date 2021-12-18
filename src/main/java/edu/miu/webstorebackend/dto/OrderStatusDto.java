package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.domain.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderStatusDto {
    private OrderStatus status;
}
