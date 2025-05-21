package com.e_commerceapp.clothshops.model;

import com.e_commerceapp.clothshops.enums.OrderStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
//@Table(name = "orders")
//we add (s) because order is a keyword in postgres
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate orderDate;

    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order",orphanRemoval = true,cascade = CascadeType.ALL)
    private Set<OrderItem> orderItems = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Orders() {
    }

    public Orders(Long id, LocalDate orderDate, BigDecimal totalPrice, OrderStatus orderStatus, Set<OrderItem> orderItems, Users user) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
        this.user = user;
    }

    public Orders(Long id, LocalDate orderDate, BigDecimal totalPrice, OrderStatus orderStatus, Set<OrderItem> orderItems) {
        this.id = id;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
        this.orderItems = orderItems;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
