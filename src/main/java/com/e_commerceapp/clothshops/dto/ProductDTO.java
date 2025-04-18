package com.e_commerceapp.clothshops.dto;

import com.e_commerceapp.clothshops.model.Category;
import lombok.Data;

import java.math.BigDecimal;

//Data annotation is used to get getter and setter immediately
@Data
public class ProductDTO {

    private Long id;
    private String name;
    private String brand;
    private BigDecimal price;
    private int inventory; //quantity
    private String description;
    private Category category;
}
