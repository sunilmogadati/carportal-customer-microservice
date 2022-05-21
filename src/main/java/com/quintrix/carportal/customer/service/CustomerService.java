package com.quintrix.carportal.customer.service;

import java.util.List;
import java.util.Optional;
import com.quintrix.carportal.customer.entity.Customer;

public interface CustomerService {

  Customer updateCustomer(Customer customer);

  String deleteCustomer(Long id);

  List<Customer> getAllCustomers();

  Customer getCustomers(String name);

  Optional<Customer> getCustomerById(Long id);

  Customer addCustomer(Customer customer);
}
