package com.quintrix.carportal.customer.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import com.quintrix.carportal.customer.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, Long> {
  @Query(value = "{'name': {$regex: /?0/, $options: 'i' }}", sort = "{ 'name' : 1 }", collation = "en")
  List<Customer> getAllByName(String name);

  Customer getByName(String name);
}
