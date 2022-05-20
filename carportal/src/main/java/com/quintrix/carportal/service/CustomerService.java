package com.quintrix.carportal.service;

import java.util.List;
import com.quintrix.jfs.customer.documents.Customer;
import com.quintrix.jfs.customer.models.GetCustomerResponse;

public interface CustomerService {

  public List<Customer> getAllCustomers();

  public Customer addCustomer(Customer customer);

  public Customer getCustomerById(Long id);

  public GetCustomerResponse getCustomers(String name);

}
