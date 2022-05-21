package com.quintrix.carportal.customer.repository;

import com.quintrix.carportal.customer.entity.Customer;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface CustomerRepository extends MongoRepository<Customer, Long> {
  @Query("{'name': {$regex: /^?0$/, $options: 'i' }}")
  List<Customer> getAllByName(String name);
}
