package com.quintrix.carportal.customer.repository;

import com.quintrix.carportal.customer.entity.Customer;
import java.util.List;
import java.util.Optional;
/*
TODO: will need to have Customer be referenced from actual implementation
  and remove the one that is be being used for testing purposes
 */
public interface CustomerRepository {
  Optional<Customer> findById(long id);

  List<Customer> findAll();

  List<Customer> findAllByName(String name);

  Customer save(Customer customer);

  void delete(Customer customer);

  void deleteById(long id);
}
