package com.example.panshippingandroid.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Shipping {

    private Long id;
    private String start;
    private String end;
    private UserModel user;
    private ProductModel product;
}
