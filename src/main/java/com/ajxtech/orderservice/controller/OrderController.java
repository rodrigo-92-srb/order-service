package com.ajxtech.orderservice.controller;

import com.ajxtech.orderservice.dto.request.OrderRequest;
import com.ajxtech.orderservice.dto.response.OrderResponse;
import com.ajxtech.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<OrderResponse> listAll(){
        return orderService.getAllOrders();
    }

    @PatchMapping("/{id}/cancel")
    public OrderResponse cancel(@PathVariable Long id){
        return orderService.cancelOrder(id);
    }
}
