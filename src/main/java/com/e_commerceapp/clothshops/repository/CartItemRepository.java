package com.e_commerceapp.clothshops.repository;

import com.e_commerceapp.clothshops.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long> {

    void deleteAllByCartId(Long cartId);
}
