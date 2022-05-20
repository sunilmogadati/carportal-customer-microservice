package com.quintrix.carportal.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepository repository;

  /*
   * Updates the customer given a customer body from controller
   */

  @Override
  public Customer updateCustomer(Customer customer) {
    Customer existingCustomer = repository.findById(major.getID()).orElse(null);
    existingCustomer.setName(customer.getName());
    existingCustomer.setEmail(customer.getEmail());
    existingCustomer.setPass(customer.getPass());
    existingCustomer.setPhone(customer.getPhone());
    existingCustomer.setStatus(customer.getStatus());
    existingCustomer.setAdress(customer.getAdress());
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
