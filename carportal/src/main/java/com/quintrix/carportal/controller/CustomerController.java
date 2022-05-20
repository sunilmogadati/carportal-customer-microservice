package com.quintrix.carportal.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.quintrix.jfs.customer.documents.Customer;
import com.quintrix.jfs.customer.models.GetCustomerResponse;
import com.quintrix.jfs.customer.service.CustomerService;


@RestController
public class CustomerController {

  @Autowired
  CustomerService customerService;

  @RequestMapping(method = RequestMethod.GET, value = "/customers")
  List<Customer> getAllCustomers() {

    return customerService.getAllCustomers();
  }

  @RequestMapping(method = RequestMethod.GET, value = "/customer")
  GetCustomerResponse getCustomers(@RequestParam(name = "name", required = false) String name) {

    return customerService.getCustomers(name);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/customer/{id}")
  Customer getCustomerDetails(@PathVariable("id") Long id) {

    if (id % 1 == 0) { // Checking for whole number
      return customerService.getCustomerById(id);
    } else {
      return null;
    }

  }


  @RequestMapping(method = RequestMethod.POST, value = "/customer")
  Customer addCustomer(@RequestBody Customer customer) {

    return customerService.addCustomer(customer);
  }
}
