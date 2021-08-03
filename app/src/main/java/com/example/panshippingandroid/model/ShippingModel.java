package com.example.panshippingandroid.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ShippingModel {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Long userId;
    private Long productId;
}
