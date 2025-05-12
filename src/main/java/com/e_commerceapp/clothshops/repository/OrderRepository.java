package com.e_commerceapp.clothshops.repository;

import com.e_commerceapp.clothshops.model.Cart;
import com.e_commerceapp.clothshops.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByUserId(Long userId);

}
