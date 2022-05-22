package com.quintrix.carportal.customer.service;

import com.quintrix.carportal.customer.exception.CustomerNotFoundException;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.repository.CustomerRepository;



@Service
public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  @Autowired
  private CustomerRepository repository;


  /*
   * Returns list of all customers
   */

  @Override
  public List<Customer> getAllCustomers() {
    logger.debug("Retruning list of all customers");
    return repository.findAll();
  }

  /*
   * Returns customers by name
   */

  @Override
  public List<Customer> getCustomers(String name) {
    logger.debug("Retruning customer with name", name);
    return repository.getAllByName(name);
  }

  /*
   * Returns customer by Id
   */

  @Override
  public Optional<Customer> getCustomerById(Long id) {
    logger.debug("Returning customer by Id", id);
    return Optional.ofNullable(getCustomerByIdOrThrowCustomerNotFoundException(id));
  }

  /*
   * Adds a new customer entity.
   */

  @Override
  public Customer addCustomer(Customer customer) {
    logger.debug("Adding a new customer to the database", customer);
    return repository.save(customer);
  }

  /*
   * Updates the customer given a customer body from controller
   */

  @Override
  public Customer updateCustomer(Customer customer) {
    Customer existingCustomer = getCustomerByIdOrThrowCustomerNotFoundException(customer.getId());
    existingCustomer.setName(customer.getName());
    existingCustomer.setEmail(customer.getEmail());
    existingCustomer.setPassword(customer.getPassword());
    existingCustomer.setPhoneNumber(customer.getPhoneNumber());
    existingCustomer.setActive(customer.isActive());
    existingCustomer.setAddress(customer.getAddress());
    logger.debug("Updating old customer", customer);
    return repository.save(existingCustomer);
  }

  /*
   * Will delete from mongo database
   */

  @Override
  public String deleteCustomer(Long id) {
    logger.debug("Deleting record with id", id);
    getCustomerByIdOrThrowCustomerNotFoundException(id);
    repository.deleteById(id);
    return "deleted customer with " + id;
  }

  private Customer getCustomerByIdOrThrowCustomerNotFoundException(Long id) {
    return repository.findById(id).orElseThrow(() -> {
      logger.error("Customer could not be found with id {}", id);
      return new CustomerNotFoundException();
    });
  }


}
