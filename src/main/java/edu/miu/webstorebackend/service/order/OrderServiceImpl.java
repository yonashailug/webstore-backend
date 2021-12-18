package edu.miu.webstorebackend.service.order;

import edu.miu.webstorebackend.Utility.MailBuilder;
import edu.miu.webstorebackend.model.Order;
import edu.miu.webstorebackend.model.OrderItem;
import edu.miu.webstorebackend.model.OrderStatus;
import edu.miu.webstorebackend.model.Product;
import edu.miu.webstorebackend.dto.*;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.repository.OrderRepository;
import edu.miu.webstorebackend.repository.ProductRepository;
import edu.miu.webstorebackend.repository.UserRepository;
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import edu.miu.webstorebackend.service.EmailService.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
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
    private final EmailService emailService;

    @Override
    public Optional<OrderResponseDto> createOrder(OrderRequestDto orderDto) {
        Order order = fromDto(orderDto);
        Order savedOrder = orderRepository.save(order);
        OrderResponseDto savedOrderDto = toDto(savedOrder);
        User user = savedOrder.getUser();
        sendProcessNotificationEmail(user.getUsername(), user.getEmail(), savedOrder.getStatus().name());
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
           User user = optionalOrder.get().getUser();
           sendProcessNotificationEmail(user.getUsername(), user.getEmail(), order.getStatus().name());
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
            User user = optionalOrder.get().getUser();
            switch (order.getStatus()) {
                case ORDERED:
                    if (newStatus == OrderStatus.ACCEPTED || newStatus == OrderStatus.CANCELED) {
                        order.setStatus(newStatus);
                        changed = true;
                        sendProcessNotificationEmail(user.getUsername(), user.getEmail(), newStatus.name());
                    }
                    break;
                case ACCEPTED:
                    if (newStatus == OrderStatus.SHIPPED ) {
                        order.setStatus(newStatus);
                        changed = true;
                        sendProcessNotificationEmail(user.getUsername(), user.getEmail(), newStatus.name());
                    }
                    break;
                case SHIPPED:
                    if (newStatus == OrderStatus.ONTHEWAY ) {
                        order.setStatus(newStatus);
                        changed = true;
                        sendProcessNotificationEmail(user.getUsername(), user.getEmail(),  newStatus.name());
                    }
                    break;
                case ONTHEWAY:
                    if (newStatus == OrderStatus.DELIVERED) {
                        order.setStatus(newStatus);
                        changed = true;
                        sendProcessNotificationEmail(user.getUsername(), user.getEmail(),  newStatus.name());
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
        order.setPayment(dto.getPayment());
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
    private void sendProcessNotificationEmail(String name, String email, String message) {
        try{
            String subject = "Order Notification";
            String messageFormatted = "Your order is "+  message + " .Thank you for using web store";
            emailService.sendEMail(email, subject, MailBuilder.buildMail(subject,name,messageFormatted));
        }
        catch (MessagingException exception) {
            System.out.println(exception.getMessage());
        }
    }
}
