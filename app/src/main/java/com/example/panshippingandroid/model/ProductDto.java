package com.example.panshippingandroid.model;


import lombok.Data;


@Data
public class ProductDto {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private UserModel user;
}


