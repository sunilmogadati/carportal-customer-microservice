package com.quintrix.carportal.customer.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.exception.CustomerNotFoundException;
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
    List<Customer> returnList;
    returnList = repository.findAll();
    if (returnList.isEmpty()) {
      logger.error("Not able to find any customers in database");
      throw new CustomerNotFoundException("No customers in database",
          "Please add information to the Database");
    } else {
      logger.debug("Retruning list of all customers");
      return returnList;
    }
  }

  /*
   * Returns customers by name
   */

  @Override
  public List<Customer> getCustomers(String name) {
    List<Customer> returnList;
    returnList = repository.getAllByName(name);
    if (returnList.isEmpty()) {
      logger.error("Not able to find any customer with name ", name);
      throw new CustomerNotFoundException("No customer with name " + name,
          "Please enter an exceptiable name for search");
    } else {
      logger.debug("Retruning customer with name", name);
      return returnList;
    }

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
    Long id = customer.getId();
    Customer newCustomer = repository.findById(id).orElse(null);
    if (newCustomer == null) {
      logger.debug("Adding a new customer to the database", customer);
      return repository.save(customer);
    } else {
      logger.error("Id is already present", id);
      throw new CustomerNotFoundException("Id is alredy present",
          "If you need to update record use diffrent function");
    }
  }

  /*
   * Updates the customer given a customer body from controller
   */

  @Override
  public Customer updateCustomer(Customer customer) {
    Long id = customer.getId();
    Customer existingCustomer = repository.findById(id).orElse(null);
    if (existingCustomer == null) {
      logger.error("No customer with id ", id);
      throw new CustomerNotFoundException("No customer with Id " + id,
          "Please enter in a customer in the database to update");
    } else {
      logger.debug("Updating old customer", customer);
      return repository.save(existingCustomer);
    }
  }

  /*
   * Will delete from mongo database
   */

  @Override
  public String deleteCustomer(Long id) {
    Customer existingCustomer = repository.findById(id).orElse(null);
    if (existingCustomer == null) {
      logger.error("No customer to delete with Id ", id);
      throw new CustomerNotFoundException("No customer with Id " + id,
          "Please enter in a valid ID");
    } else {
      logger.debug("Deleting record with id", id);
      repository.deleteById(id);
      return "deleted customer with " + id;
    }
  }

  private Customer getCustomerByIdOrThrowCustomerNotFoundException(Long id) {
    return repository.findById(id).orElseThrow(() -> {
      logger.error("Customer could not be found with id {}", id);
      return new CustomerNotFoundException();
    });
  }


}
