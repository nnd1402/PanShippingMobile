package com.example.panshippingandroid.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductShipping {
    private Long id;
    private String name;
    private double price;
    private int quantity;
    private String description;
    private String image;
    private UserModel user;
    private List<Shipping> shipping;
}
