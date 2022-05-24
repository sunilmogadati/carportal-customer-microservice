package com.quintrix.carportal.customer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.quintrix.carportal.customer.entity.CustomerUser;

@Repository
public interface UserRepository extends JpaRepository<CustomerUser, Long> {

  CustomerUser findByUsername(String username);
}
