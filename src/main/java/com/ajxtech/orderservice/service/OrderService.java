package com.ajxtech.orderservice.service;

import com.ajxtech.orderservice.dto.OrderRequest;
import com.ajxtech.orderservice.dto.OrderResponse;
import com.ajxtech.orderservice.exception.OrderNotFoundException;
import com.ajxtech.orderservice.model.Order;
import com.ajxtech.orderservice.model.OrderStatus;
import com.ajxtech.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderResponse createOrder(OrderRequest request){
        log.info("Creating order for customer: {}", request.getCustomerName());
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerEmail(request.getCustomerEmail());
        order.setTotalAmount(request.getTotalAmount());

        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());

        return convertToResponse(savedOrder);
    }

    private OrderResponse convertToResponse(Order order){
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCustomerName(order.getCustomerName());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());

        return response;
    }

    public List<OrderResponse> getAllOrders(){
        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public OrderResponse cancelOrder(Long id){
        log.info("Attempting to cancel order with ID: {}", id);
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Failed to cancel: Order {} not found", id);
                    return new OrderNotFoundException("Order not found with id: "+id);
                });

        order.setStatus(OrderStatus.CANCELED);
        Order savedOrder = orderRepository.save(order);

        log.info("Order {} canceled successfully", id);
        return convertToResponse(savedOrder);
    }
}
