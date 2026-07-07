package com.ajxtech.orderservice.service;

import com.ajxtech.orderservice.dto.request.OrderRequest;
import com.ajxtech.orderservice.dto.response.OrderResponse;
import com.ajxtech.orderservice.exception.OrderNotFoundException;
import com.ajxtech.orderservice.model.Order;
import com.ajxtech.orderservice.model.OrderStatus;
import com.ajxtech.orderservice.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("Should create an order with success")
    void shouldCreateOrderWithSuccess(){
        // GIVEN
        OrderRequest request = new OrderRequest();
        request.setCustomerName("Elon Musk");
        request.setTotalAmount(new BigDecimal("500.00"));

        Order orderSaved = new Order();
        orderSaved.setId(1L);
        orderSaved.setCustomerName("Elon Musk");
        orderSaved.setStatus(OrderStatus.PENDING);

        // WHEN
        when(orderRepository.save(any(Order.class))).thenReturn(orderSaved);

        // WHEN
        OrderResponse response = orderService.createOrder(request);

        // THEN
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(OrderStatus.PENDING, response.getStatus());

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should cancel an order with success")
    void shouldCancelOrderWithSucess(){
        // GIVEN
        Long orderId = 1L;
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setStatus(OrderStatus.PENDING);

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        when(orderRepository.save(any(Order.class))).thenReturn(existingOrder);

        OrderResponse response = orderService.cancelOrder(orderId);

        // THEN
        assertNotNull(response);
        assertEquals(OrderStatus.CANCELED, response.getStatus());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("Should throw exception when order to cancel doesn't exist")
    void shouldThrowExceptionWhenOrderDoesNotExist(){
        // GIVEN
        Long orderId = 99L;

        // WHEN
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.cancelOrder(orderId);
        });
        // THEN
        verify(orderRepository, never()).save(any(Order.class));

    }

}