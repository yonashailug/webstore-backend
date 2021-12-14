package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.dto.OrderDto;
import edu.miu.webstorebackend.dto.OrderStatusResponse;
import edu.miu.webstorebackend.security.services.UserDetailsImpl;
import edu.miu.webstorebackend.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAll() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        List<OrderDto> orderDtos = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orderDtos);
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@RequestBody OrderDto orderDto) {
        Optional<OrderDto> optionalOrderDto = orderService.createOrder(orderDto);
        if(optionalOrderDto.isPresent()) {
            return ResponseEntity.ok(optionalOrderDto.get());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("{id}/cancel")
    @PreAuthorize("hasRole('Buyer')")
    public ResponseEntity<OrderStatusResponse> cancelOrder(@PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if(orderService.isOrderBelongToUser(id, userId)) {
            Optional<OrderDto> optionalOrderDto = orderService.cancelOrder(id);
            if (optionalOrderDto.isPresent()) {
                OrderDto orderDto = optionalOrderDto.get();
                if(orderDto.getStatus() == OrderStatus.CANCELED) {
                    return ResponseEntity.ok(new OrderStatusResponse("Order Successfully canceled", null));
                }
                else {
                    return ResponseEntity.badRequest().body(new OrderStatusResponse(null,
                            "Can not cancel the order. Order is already " + orderDto.getStatus()));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderStatusResponse(null, "Order with id " + id + " not found"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("{id}/status")
    @PreAuthorize("hasRole('Seller')")
    public ResponseEntity<OrderStatusResponse> updateOrder(@RequestBody OrderStatus status, @PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if(orderService.isOrderBelongToUser(id, userId)) {
            Optional<OrderDto> optionalOrderDto = orderService.changeStatus(status, id);
            if (optionalOrderDto.isPresent()) {
                OrderDto orderDto = optionalOrderDto.get();
                if(orderDto.getStatus() == status) {
                    return ResponseEntity.ok(new OrderStatusResponse("Successfully updated to " + status, null));
                }
                else {
                    return ResponseEntity.badRequest().body(new OrderStatusResponse(null, "Can not change the status from " + orderDto.getStatus() + "to " + status));
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderStatusResponse(null, "Order with id " + id + " not found"));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
