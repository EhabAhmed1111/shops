package com.e_commerceapp.clothshops.data.repository;

import com.e_commerceapp.clothshops.data.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users,Long> {


    Boolean existsByEmail(String userEmail);
}
