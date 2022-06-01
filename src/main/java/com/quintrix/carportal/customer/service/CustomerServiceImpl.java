package com.quintrix.carportal.customer.service;

import java.util.ArrayList;
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

  public CustomerServiceImpl(CustomerRepository customerRepository) {
    this.repository = customerRepository;
  }
  /*
   * Returns list of all ClientCustomer without the id for security
   */

  @Override
  public List<ClientCustomer> getAllCustomers() {
    List<Customer> customerList;
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

  @SuppressWarnings("unchecked")
  @Override
  public <T> List<T> getCustomers(String name) {

    if (name == null) {
      return (List<T>) getAllCustomers();
    } else {

      List<Customer> customerList;
      customerList = repository.getAllByName(name);

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
   * Returns customers by car id
   */
  @Override
  public List<ClientCustomer> getCustomerByCar(String id) {
    List<Customer> customerList = repository.findAll();
    List<ClientCustomer> clientCustomerList;
    List<Customer> customerCarList = customerList.stream()
        .filter(c -> c.getOwnedCars().contains(id)).collect(Collectors.toList());
    if (customerCarList.isEmpty()) {
      logger.error("No customer has a car id ", id);
      throw new CustomerNotFoundException("No customer with car id " + id,
          "Please enter in a car id that is valid.");
    } else {
      logger.debug("Returning List of customers with car id ", id);
      clientCustomerList =
          customerCarList.stream().map(c -> new ClientCustomer(c.getName(), c.getEmail(),
              c.getPhoneNumber(), c.getAddress(), c.getOwnedCars())).collect(Collectors.toList());
      return clientCustomerList;
    }
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
   * Returns list of customerClients based on multiple variables
   */


  @Override
  public List<ClientCustomer> search(String name, String address, String phone, String email) {
    Integer nameParam = 0;
    Integer addressParam = 0;
    Integer phoneParam = 0;
    Integer emailParam = 0;
    List<ClientCustomer> returnList = new ArrayList<ClientCustomer>();
    if (name != null) {
      nameParam = 1;
    }
    if (address != null) {
      addressParam = 1;
    }
    if (phone != null) {
      phoneParam = 1;
    }
    if (email != null) {
      emailParam = 1;
    }
    switch (nameParam + addressParam + phoneParam + emailParam) {
      case 0:
        // Searching for all customers because no parameters were entered
        logger.debug("No search paramaters returning list of all customers");
        return getAllCustomers();
      case 1:
        if (nameParam == 1) {
          // Searching for all customers by specific name
          logger.debug("Searching for name {}", name);
          return getCustomers(name);
        } else if (phoneParam == 1) {
          // Searching for all customers by phone number
          logger.debug("Searching for phone number {}", phone);
          return getCustomerByPhoneNumber(phone);
        } else if (addressParam == 1) {
          // Searching for all customers by address
          logger.debug("Searching for address {}", address);
          return getCustomerByAddress(address);
        } else if (emailParam == 1) {
          // Searching for all customers by email
          logger.debug("Searching for email {}", email);
          return getCustomerByEmail(email);
        } else {
          logger.error("Should not have reached this point");
          throw new IllegalStateException();
        }
      case 2:
        if (nameParam == 1 && phoneParam == 1) {
          // Searching for all customers by specific name and phone number
          logger.debug("Searching for name {} and phone number {}", name, phone);
          returnList.addAll(getCustomers(name));
          returnList.addAll(getCustomerByPhoneNumber(phone));
          return returnList;
        } else if (nameParam == 1 && addressParam == 1) {
          // Searching for all customers by specific name and address
          logger.debug("Searching for name {} and address {}", name, address);
          returnList.addAll(getCustomers(name));
          returnList.addAll(getCustomerByAddress(address));
          return returnList;
        } else if (nameParam == 1 && emailParam == 1) {
          // Searching for all customers by specific name and email
          logger.debug("Searching for name {} and email {}", name, email);
          returnList.addAll(getCustomers(name));
          returnList.addAll(getCustomerByEmail(email));
          return returnList;
        } else if (phoneParam == 1 && addressParam == 1) {
          // Searching for all customers by phone number and address
          logger.debug("Searching for phone {} and address {}", phone, address);
          returnList.addAll(getCustomerByPhoneNumber(phone));
          returnList.addAll(getCustomerByAddress(address));
          return returnList;
        } else if (phoneParam == 1 && emailParam == 1) {
          // Searching for all customers by phone number and email
          logger.debug("Searching for phone {} and email {}", phone, email);
          returnList.addAll(getCustomerByPhoneNumber(phone));
          returnList.addAll(getCustomerByEmail(email));
          return returnList;
        } else if (addressParam == 1 && emailParam == 1) {
          // Searching for all customers by email and address
          logger.debug("Searching for address {} and email {}", address, email);
          returnList.addAll(getCustomerByAddress(address));
          returnList.addAll(getCustomerByEmail(email));
          return returnList;
        } else {
          logger.error("Should not have reached this point");
          throw new IllegalStateException();
        }
      case 3:
        if (nameParam == 1 && phoneParam == 1 && addressParam == 1) {
          // Searching for all customers by specific name and phone number and address
          logger.debug("Searching for name {} and phone {} and address {}", name, phone, address);
          returnList.addAll(getCustomers(name));
          returnList.addAll(getCustomerByPhoneNumber(phone));
          returnList.addAll(getCustomerByAddress(address));
          return returnList;
        } else if (nameParam == 1 && phoneParam == 1 && emailParam == 1) {
          // Searching for all customers by specific name and phone number and email
          logger.debug("Searching for name {} and phone {} and email {}", name, phone, email);
          returnList.addAll(getCustomers(name));
          returnList.addAll(getCustomerByPhoneNumber(phone));
          returnList.addAll(getCustomerByEmail(email));
          return returnList;
        } else if (nameParam == 1 && emailParam == 1 && addressParam == 1) {
          // Searching for all customers by specific name and address and email
          logger.debug("Searching for name {} and email {} and address {}", name, email, address);
          returnList.addAll(getCustomers(name));
          returnList.addAll(getCustomerByEmail(email));
          returnList.addAll(getCustomerByAddress(address));
          return returnList;
        } else if (phoneParam == 1 && emailParam == 1 && addressParam == 1) {
          // Searching for all customers by email and phone number and address
          logger.debug("Searching for phone {} and email {} and address {}", phone, email, address);
          returnList.addAll(getCustomerByPhoneNumber(phone));
          returnList.addAll(getCustomerByEmail(email));
          returnList.addAll(getCustomerByAddress(address));
          return returnList;
        } else {
          logger.debug("This section should not be reached");
          throw new IllegalStateException();
        }
      case 4:
        // Searching for all customers by all search parameters
        returnList.addAll(getCustomers(name));
        returnList.addAll(getCustomerByPhoneNumber(phone));
        returnList.addAll(getCustomerByEmail(email));
        returnList.addAll(getCustomerByAddress(address));
        return returnList;
    }
    logger.error("Out of Switch statement sould not be here");
    throw new IllegalStateException();
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
