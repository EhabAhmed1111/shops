package com.e_commerceapp.clothshops.data.repository;

import com.e_commerceapp.clothshops.data.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
}
