package com.quintrix.carportal.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.quintrix.jfs.customer.documents.Customer;
import com.quintrix.jfs.customer.service.CustomerService;

// Need to add error handling somewhere
@RestController
public class CustomerController {

  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  @Autowired
  CustomerService customerService;

  @RequestMapping(method = RequestMethod.GET, value = "/customers")
  List<Customer> getAllCustomers() {

    return customerService.getAllCustomers();
  }

  // Retrieves complete customer information including id.
  @RequestMapping(method = RequestMethod.GET, value = "/customer")
  Customer getCustomers(@RequestParam(name = "name", required = false) String name) {

    logger.debug("Request: Searrch records for name: {}", name);
    return customerService.getCustomers(name);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/customer/{id}")
  Customer getCustomerDetails(@PathVariable("id") Long id) {

    logger.debug("Request: Called getCustomerById Controller {}", id);
    return customerService.getCustomerById(id);

  }

  @RequestMapping(method = RequestMethod.POST, value = "/customer")
  Customer addCustomer(@RequestBody Customer customer) {

    logger.debug("Request: Added new customer: {}", customer);
    return customerService.addCustomer(customer);
  }
}
