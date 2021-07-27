package com.example.panshippingandroid.model;


import lombok.Data;


@Data
public class ProductModel {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private Long user;
}


