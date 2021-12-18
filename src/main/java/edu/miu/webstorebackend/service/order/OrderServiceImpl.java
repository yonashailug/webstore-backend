package edu.miu.webstorebackend.service.order;

import edu.miu.webstorebackend.domain.Order;
import edu.miu.webstorebackend.domain.OrderItem;
import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.domain.Product;
import edu.miu.webstorebackend.dto.*;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.repository.OrderRepository;
import edu.miu.webstorebackend.repository.ProductRepository;
import edu.miu.webstorebackend.repository.UserRepository;
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final GenericMapper mapper;

    @Override
    public Optional<OrderResponseDto> createOrder(OrderRequestDto orderDto) {
        Order order = fromDto(orderDto);
        Order savedOrder = orderRepository.save(order);
        OrderResponseDto savedOrderDto = toDto(savedOrder);
        return Optional.of(savedOrderDto);
    }

    @Override
    public Optional<OrderResponseDto> cancelOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isPresent()) {
           Order order = optionalOrder.get();
           OrderStatus status = order.getStatus();
           if(status == OrderStatus.ORDERED || status == OrderStatus.ACCEPTED) {
                order.setStatus(OrderStatus.CANCELED);
                orderRepository.save(order);
           }
            OrderResponseDto dto = toDto(order);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public List<OrderResponseDto> getOrdersByUserId(Long id) {
        List<Order> orders = orderRepository.findOrdersByUserId(id);
        List<OrderResponseDto> dtos = orders.stream().map(order -> toDto(order)).collect(Collectors.toList());
        return dtos;
    }

    @Override
    public Optional<OrderResponseDto> changeStatus(OrderStatus newStatus, Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        //check if user owns the order
        if(optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            boolean changed = false;
            switch (order.getStatus()) {
                case ORDERED:
                    if (newStatus == OrderStatus.ACCEPTED || newStatus == OrderStatus.CANCELED) {
                        order.setStatus(newStatus);
                        changed = true;
                    }
                    break;
                case ACCEPTED:
                    if (newStatus == OrderStatus.SHIPPED || newStatus == OrderStatus.CANCELED) {
                        order.setStatus(newStatus);
                        changed = true;
                    }
                    break;
                case SHIPPED:
                    if (newStatus == OrderStatus.ONTHEWAY || newStatus == OrderStatus.CANCELED) {
                        order.setStatus(newStatus);
                        changed = true;
                    }
                    break;
                case ONTHEWAY:
                    if (newStatus == OrderStatus.DELIVERED || newStatus == OrderStatus.CANCELED) {
                        order.setStatus(newStatus);
                        changed = true;
                    }
                    break;
            }
            if(changed) {
                orderRepository.save(order);
            }
            OrderResponseDto dto = toDto(order);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public boolean isOrderBelongToUser(Long orderId, Long userId) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if(optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if(order.getUser().getId() == userId) {
                return true;
            }
        }
        return false;
    }
    
    private OrderResponseDto toDto(Order order) {
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setStatus(order.getStatus());
        dto.setBillingAddress(order.getBillingAddress());
        dto.setShippingAddress(order.getShippingAddress());

        dto.setOrderItems(order.getOrderItems().stream().map(orderItem -> {
            OrderItemResponseDto orderItemResponseDto = new OrderItemResponseDto();
            orderItemResponseDto.setProduct((ProductDto) mapper.mapObject(orderItem.getProduct(), ProductDto.class));
            orderItemResponseDto.setQuantity(orderItem.getQuantity());
            orderItemResponseDto.setStatus(orderItem.getStatus());
            return orderItemResponseDto;
        }).collect(Collectors.toList()));

        return dto;
    }

    private Order fromDto(OrderRequestDto dto) {
        Order order = new Order();
        List<OrderItemRequestDto> items = dto.getOrderItems();

        List<OrderItem> orderItems = items.stream().map(item -> {
            Product p = productRepository.getById(item.getProductId());
            OrderItem oItem = new OrderItem();
            oItem.setProduct(p);
            oItem.setOrder(order);
            oItem.setStatus(OrderStatus.ORDERED);
            oItem.setQuantity(item.getQuantity());
            return oItem;
        }).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setOrderDate(LocalDateTime.now());
        order.setBillingAddress(dto.getBillingAddress());
        order.setShippingAddress(dto.getShippingAddress());
        order.setStatus(OrderStatus.ORDERED);

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        Optional<User> optional = userRepository.findById(userId);
        order.setUser(optional.get());
        return order;
    }

    @Override
    public List<OrderResponseDto> getOrdersForSeller(Long id) {
        List<Order> orders = orderRepository.findAll()
                .stream()
                .filter(order -> order.getOrderItems().stream().anyMatch(o -> o.getProduct().getSeller().getId() == id)).collect(Collectors.toList());
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }
}
