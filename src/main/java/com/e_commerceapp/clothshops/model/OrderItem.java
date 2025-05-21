package com.e_commerceapp.clothshops.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int quantity;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Orders order;

    public OrderItem() {
    }

    public OrderItem(Orders order, Product product, BigDecimal price, int quantity) {
        this.order = order;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    public OrderItem(Long id, int quantity, BigDecimal price, Product product, Orders order) {
        this.id = id;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Orders getOrder() {
        return order;
    }

    public void setOrder(Orders order) {
        this.order = order;
    }
}
