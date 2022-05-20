package com.quintrix.carportal.customer.repository;

import java.util.List;
import java.util.Optional;
/*
TODO: will need to have Customer be referenced from actual implementation
  and remove the one that is be being used for testing purposes
 */
public interface CustomerRepository {
  Optional<Customer> findById(String id);

  List<Customer> findAll();

  Customer save(Customer customer);

  void delete(Customer customer);

  void deleteById(String id);
}
