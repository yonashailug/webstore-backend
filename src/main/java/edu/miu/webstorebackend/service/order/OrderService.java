package edu.miu.webstorebackend.service.order;

import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.dto.OrderRequestDto;
import edu.miu.webstorebackend.dto.OrderResponseDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface OrderService {
    Optional<OrderResponseDto> createOrder(OrderRequestDto orderDto);
    Optional<OrderResponseDto> cancelOrder(Long id);
    List<OrderResponseDto> getOrdersByUserId(Long id);
    Optional<OrderResponseDto> changeStatus(OrderStatus newStatus, Long id);
    boolean isOrderBelongToUser(Long orderId, Long userId);
}
