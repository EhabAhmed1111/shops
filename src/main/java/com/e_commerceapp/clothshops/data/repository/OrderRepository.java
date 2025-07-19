package com.e_commerceapp.clothshops.data.repository;

import com.e_commerceapp.clothshops.data.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders,Long> {

    List<Orders> findAllByUserId(Long userId);

}
