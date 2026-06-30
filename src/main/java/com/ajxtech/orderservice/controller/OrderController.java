package com.ajxtech.orderservice.controller;

import com.ajxtech.orderservice.dto.OrderRequest;
import com.ajxtech.orderservice.dto.OrderResponse;
import com.ajxtech.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@RequestBody OrderRequest request){
        return orderService.createOrder(request);
    }
}
