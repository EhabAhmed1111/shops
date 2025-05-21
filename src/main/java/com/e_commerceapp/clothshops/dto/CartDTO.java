package com.e_commerceapp.clothshops.dto;

import java.math.BigDecimal;
import java.util.Set;

public class CartDTO {


    private Long id;
    private BigDecimal totalPrice ;
    private Set<CartItemDTO> items;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Set<CartItemDTO> getItems() {
        return items;
    }

    public void setItems(Set<CartItemDTO> items) {
        this.items = items;
    }
}
