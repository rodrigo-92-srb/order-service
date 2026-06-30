package com.ajxtech.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequest {
    private String customerName;
    private String customerEmail;
    private BigDecimal totalAmount;
}
