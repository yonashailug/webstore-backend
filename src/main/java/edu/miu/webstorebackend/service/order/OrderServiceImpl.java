package edu.miu.webstorebackend.service.order;

import edu.miu.webstorebackend.domain.Order;
import edu.miu.webstorebackend.domain.OrderStatus;
import edu.miu.webstorebackend.dto.OrderDto;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final GenericMapper mapper;
    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public Optional<OrderDto> createOrder(OrderDto orderDto) {
        Order order = modelMapper.map(orderDto, Order.class);
        orderRepository.save(order);
        OrderDto savedOrderDto = modelMapper.map(order, OrderDto.class);
        return Optional.of(savedOrderDto);
    }

    @Override
    public Optional<OrderDto> cancelOrder(Long id) {
        Optional<Order> optionalOrder = orderRepository.findById(id);
        if(optionalOrder.isPresent()) {
           Order order = optionalOrder.get();
           OrderStatus status = order.getStatus();
           if(status == OrderStatus.ORDERED || status == OrderStatus.ACCEPTED) {
                order.setStatus(OrderStatus.CANCELED);
                orderRepository.save(order);
           }
            OrderDto dto = (OrderDto)mapper.mapObject(order, OrderDto.class);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public List<OrderDto> getOrdersByUserId(Long id) {
        List<Order> orders = orderRepository.findOrdersByUserId(id);
        List<OrderDto> dtos = mapper.mapList(orders, OrderDto.class);
        return dtos;
    }

    @Override
    public Optional<OrderDto> changeStatus(OrderStatus newStatus, Long id) {
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
            OrderDto dto = (OrderDto)mapper.mapObject(order, OrderDto.class);
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
}
