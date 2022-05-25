package com.quintrix.carportal.customer.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quintrix.carportal.customer.entity.ClientCustomer;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.exception.CustomerNotFoundException;
import com.quintrix.carportal.customer.repository.CustomerRepository;



@Service
public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  @Autowired
  private CustomerRepository repository;


  /*
   * Returns list of all ClientCustomer without the id for security
   */

  @Override
  public List<ClientCustomer> getAllCustomers() {
    List<Customer> customerList;
    List<ClientCustomer> clientCustomerList;
    customerList = getAllCustomersAdmin();
    return getAClientCustomerList(customerList);
  }

  /*
   * Returns a list of Customers from the database with the Id's for Admin's.
   */
  @Override
  public List<Customer> getAllCustomersAdmin() {
    List<Customer> returnList = repository.findAll();
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
   * Returns client customers by name
   */

  @Override
  public <T> List<T> getCustomers(String name) {

    if (name == null) {
      return (List<T>) getAllCustomers();
    } else {

      List<Customer> customerList;
      customerList = repository.getAllByName(name);

      List<ClientCustomer> clientCustomerList = null;

      if (customerList.isEmpty()) {
        logger.error("Not able to find any customer with name ", name);
        throw new CustomerNotFoundException("No customer with name " + name,
            "Please enter an acceptiable name for search");
      } else {
        logger.debug("Retruning customer with name", name);

        return (List<T>) getAClientCustomerList(customerList);
      }
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
   * Returns list of ClientCustomer by phone number
   */
  @Override
  public List<ClientCustomer> getCustomerByPhoneNumber(String phone) {
    List<Customer> customerList = repository.getByPhoneNumber(phone);
    if (customerList.isEmpty()) {
      logger.error("No customer with Phone Number ", phone);
      throw new CustomerNotFoundException("No customer with phone number " + phone,
          "Please enter in an acceptable phone number");
    } else {
      logger.debug("Returning list of customers with phone number ", phone);
      return getAClientCustomerList(customerList);
    }
  }

  /*
   * Returns a list of ClientCustomers by address
   */

  @Override
  public List<ClientCustomer> getCustomerByAddress(String address) {
    List<Customer> customerList = repository.getByAddress(address);
    if (customerList.isEmpty()) {
      logger.error("No customer with address ", address);
      throw new CustomerNotFoundException("No customer with address " + address,
          "Please enter in an acceptable address");
    } else {
      logger.debug("Returing list of ClientCustomers with address ", address);
      return getAClientCustomerList(customerList);
    }
  }

  /*
   * Returns customers by car id TODO
   */
  @Override
  public List<ClientCustomer> getCustomerByCar(String id) {
    return null;
  }


  /*
   * Returns list of customerClients based on email
   */
  @Override
  public List<ClientCustomer> getCustomerByEmail(String email) {
    List<Customer> customerList = repository.getByEmail(email);
    if (customerList.isEmpty()) {
      logger.error("No customer with email ", email);
      throw new CustomerNotFoundException("No customer with email " + email,
          "Please enter in an acceptable email");
    } else {
      logger.debug("Returing list of ClientCustomers with email ", email);
      return getAClientCustomerList(customerList);
    }
  }

  /*
   * Returns list of customerClients based on multiple variables TODO
   */

  @Override
  public List<ClientCustomer> search(String name, String address, String phone, String email) {

    return null;
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
      return repository.save(customer);
    }
  }

  /*
   * Will delete one record from Mongo database
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

  @Override
  public List<ClientCustomer> getAClientCustomerList(List<Customer> customerList) {

    List<ClientCustomer> clientCustomerList;
    clientCustomerList =
        customerList.stream().map(c -> new ClientCustomer(c.getName(), c.getEmail(),
            c.getPhoneNumber(), c.getAddress(), c.getOwnedCars())).collect(Collectors.toList());

    return clientCustomerList;
  }


}
