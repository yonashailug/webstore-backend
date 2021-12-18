package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.dto.*;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import edu.miu.webstorebackend.service.UserService.UserService;
import edu.miu.webstorebackend.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAll() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        List<OrderResponseDto> orderDtos;

        if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SELLER"))) {
            orderDtos = orderService.getOrdersForSeller(userId);
        } else {
            orderDtos = orderService.getOrdersByUserId(userId);
        }
        return ResponseEntity.ok(orderDtos);
    }

    @PostMapping
    public ResponseEntity<OrderResponseDto> create(@RequestBody OrderRequestDto orderDto) {
        Optional<OrderResponseDto> optionalOrderDto = orderService.createOrder(orderDto);
        if(optionalOrderDto.isPresent()) {
            return ResponseEntity.ok(optionalOrderDto.get());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("{id}/cancel")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<OrderStatusResponse> cancelOrder(@PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if(orderService.isOrderBelongToUser(id, userId)) {
            Optional<OrderResponseDto> optionalOrderDto = orderService.cancelOrder(id);
            if (optionalOrderDto.isPresent()) {
                OrderResponseDto orderDto = optionalOrderDto.get();
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
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<OrderStatusResponse> updateOrder(@RequestBody OrderStatusDto status, @PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();

        Optional<OrderResponseDto> optionalOrderDto = orderService.changeStatus(status.getStatus(), id);
        if (optionalOrderDto.isPresent()) {
            OrderResponseDto orderDto = optionalOrderDto.get();
            if(orderDto.getStatus() == status.getStatus()) {
                return ResponseEntity.ok(new OrderStatusResponse("Successfully updated to " + status.getStatus(), null));
            }
            else {
                return ResponseEntity.badRequest().body(new OrderStatusResponse(null, "Can not change the status from " + orderDto.getStatus() + "to " + status.getStatus()));
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new OrderStatusResponse(null, "Order with id " + id + " not found"));
        }
        //FIXME- check if the seller owns the product
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @GetMapping("/seller")
    public ResponseEntity<List<OrderResponseDto>> getOrdersForSeller() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        List<OrderResponseDto> orderDtos = orderService.getOrdersByUserId(userId);
        return ResponseEntity.ok(orderDtos);
    }
}
