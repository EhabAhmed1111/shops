package com.e_commerceapp.clothshops.repository;

import com.e_commerceapp.clothshops.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {

//    "select cart from cart join user on user_id when user.id := userId"

    Boolean existsByEmail(String userEmail);
}
