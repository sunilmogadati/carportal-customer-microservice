package com.quintrix.carportal.customer.service;

import com.quintrix.carportal.customer.entity.Customer;

public interface CustomerService {

  Customer updateCustomer(Customer customer);

  String deleteCustomer(Long id);
}
