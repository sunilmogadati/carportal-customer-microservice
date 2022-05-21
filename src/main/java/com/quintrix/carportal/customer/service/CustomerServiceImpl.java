package com.quintrix.carportal.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.repository.CustomerRepository;


@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository repository;

  /*
   * Updates the customer given a customer body from controller
   */

  @Override
  public Customer updateCustomer(Customer customer) {
    Customer existingCustomer = repository.findById(customer.getId()).orElse(null);
    existingCustomer.setName(customer.getName());
    existingCustomer.setEmail(customer.getEmail());
    existingCustomer.setPassword(customer.getPassword());
    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
    existingCustomer.setActive(customer.isActive());
    existingCustomer.setAddress(customer.getAddress());
    return repository.save(existingCustomer);
  }

  /*
   * Will delete from mongo database
   */

  @Override
  public String deleteCustomer(Long id) {
    repository.deleteById(id);
    return "deleted customer with " + id;
  }



}
