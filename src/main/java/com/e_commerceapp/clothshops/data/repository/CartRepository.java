package com.e_commerceapp.clothshops.data.repository;

import com.e_commerceapp.clothshops.data.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

     Cart findByUserId(Long userId);
}
