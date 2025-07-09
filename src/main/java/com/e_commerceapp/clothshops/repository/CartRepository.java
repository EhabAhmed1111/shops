package com.e_commerceapp.clothshops.repository;

import com.e_commerceapp.clothshops.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

     Cart findByUserId(Long userId);
}
