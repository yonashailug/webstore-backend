package edu.miu.webstorebackend.service.order;

import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.dto.OrderDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    Optional<OrderDto> createOrder(OrderDto orderDto);
    Optional<OrderDto> cancelOrder(Long id);
    List<OrderDto> getOrdersByUserId(Long id);
    Optional<OrderDto> changeStatus(OrderStatus newStatus, Long id);
    boolean isOrderBelongToUser(Long orderId, Long userId);
}
