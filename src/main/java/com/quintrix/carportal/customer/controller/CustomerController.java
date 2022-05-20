package com.quintrix.carportal.customer.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

  // @Autowired
  // CustomerService service;

  Logger logger = LoggerFactory.getLogger(CustomerController.class);

  // TODO: get all customers

  // TODO: get one customer info by id or other method

  // TODO: add/register customer

  // TODO: customer log in?

  // update customer: name/email/password/phone/active/address/
  @PutMapping("/customer/update")
  public Customer updateCustomerInformation(@RequestBody Customer customer) {
    logger.debug("Request: update information Customer {}", customer);
    return service.updateCustomer(customer);
  }

  // delete customer
  @DeleteMapping("/customers/{id}")
  public void deleteCustomerById(@PathVariable Long id) {
    logger.debug("Request: delete Customer with id {}", id);
    return service.deleteCustomer(id);
  }

}
