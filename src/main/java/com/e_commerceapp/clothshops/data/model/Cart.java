package com.e_commerceapp.clothshops.data.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalPrice = BigDecimal.ZERO;


    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> items = new HashSet<>();


    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Cart() {
    }

    public Cart(Long id, BigDecimal totalPrice, Set<CartItem> items, Users user) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.items = items;
        this.user = user;
    }

    public Cart(Long id, BigDecimal totalPrice, Set<CartItem> items) {
        this.id = id;
        this.totalPrice = totalPrice;
        this.items = items;
    }

    public void addItem(CartItem item) {
        this.items.add(item);
        item.setCart(this);
        updateTotalPrice();
    }

    public void removeItem(CartItem item) {
        this.items.remove(item);
        item.setCart(null);
        updateTotalPrice();
    }

    private void updateTotalPrice() {

        this.totalPrice = items
                .stream()
                .map(item -> {
                    BigDecimal unitePrice = item.getUnitePrice();
                    if (unitePrice == null) {
                        return BigDecimal.ZERO;
                    }
                    return unitePrice.multiply(BigDecimal.valueOf(item.getQuantity()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

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

    public Set<CartItem> getItems() {
        return items;
    }

    public void setItems(Set<CartItem> items) {
        this.items = items;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
