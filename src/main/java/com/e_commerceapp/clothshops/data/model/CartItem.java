package com.e_commerceapp.clothshops.data.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal unitePrice;

    private BigDecimal totalPrice;

    private int quantity;

    @ManyToOne()
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne()
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    public CartItem() {
    }

    public CartItem(Cart cart, Product product, int quantity, BigDecimal unitePrice) {
        this.cart = cart;
        this.product = product;
        this.quantity = quantity;
//        this.totalPrice = totalPrice;
        this.unitePrice = unitePrice;
    }

    public CartItem(Long id, BigDecimal unitePrice, BigDecimal totalPrice, Product product, Cart cart) {
        this.id = id;
        this.unitePrice = unitePrice;
        this.totalPrice = totalPrice;
        this.product = product;
        this.cart = cart;
    }

    public void setTotalPrice(){
        this.totalPrice = this.unitePrice.multiply(new BigDecimal(this.quantity));
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

    public BigDecimal getUnitePrice() {
        return unitePrice;
    }

    public void setUnitePrice(BigDecimal unitePrice) {
        this.unitePrice = unitePrice;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }
}
