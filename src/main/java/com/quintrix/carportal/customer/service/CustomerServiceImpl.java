package com.quintrix.carportal.customer.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.quintrix.carportal.customer.entity.ClientCustomer;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.exception.CustomerNotFoundException;
import com.quintrix.carportal.customer.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

  private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

  public static long totalResults;
  public static int totalPages;

  @Autowired
  private CustomerRepository repository;

  public CustomerServiceImpl(CustomerRepository customerRepository) {
    this.repository = customerRepository;
  }
  /*
   * Returns list of all ClientCustomer without the id for security
   */

  @Override
  public List<ClientCustomer> getAllCustomers(int offSet, int pageSize) {
    Pageable pageable = PageRequest.of(offSet, pageSize);
    Page<Customer> page = repository.findAll(pageable);
    List<Customer> customerList = page.getContent();
    totalResults = page.getTotalElements();
    totalPages = page.getTotalPages();
    return getAClientCustomerList(customerList);
  }

  /*
   * Returns a list of Customers from the database with the Id's for Admin's.
   */
  @Override
  public List<Customer> getAllCustomersAdmin(int offSet, int pageSize) {
    Pageable pageable = PageRequest.of(offSet, pageSize);
    Page<Customer> returnPage = repository.findAll(pageable);
    List<Customer> returnList = returnPage.toList();

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
  public List<ClientCustomer> getCustomers(String name, int offSet, int pageSize) {
    Pageable pageable = PageRequest.of(offSet, pageSize);
    if (name == null) {

      return getAllCustomers(offSet, pageSize);
    } else {

      Page<Customer> page = repository.getAllByName(name, pageable);
      List<Customer> customerList = page.getContent();
      totalResults = page.getTotalElements();
      totalPages = page.getTotalPages();

      if (customerList.isEmpty()) {
        logger.error("Not able to find any customer with name ", name);
        throw new CustomerNotFoundException("No customer with name " + name,
            "Please enter an acceptiable name for search");
      } else {
        logger.debug("Retruning customer with name {}", name);

        return getAClientCustomerList(customerList);
      }
    }
  }

  /*
   * Returns customer by Id
   */

  @Override
  public Optional<Customer> getCustomerById(Long id) {
    logger.debug("Returning customer by Id {}", id);
    return Optional.ofNullable(getCustomerByIdOrThrowCustomerNotFoundException(id));
  }


  /*
   * Returns list of ClientCustomer by phone number
   */
  @Override
  public List<ClientCustomer> getCustomerByPhoneNumber(String phone, int offSet, int pageSize) {
    Pageable pageable = PageRequest.of(offSet, pageSize);
    Page<Customer> page = repository.getByPhoneNumber(phone, pageable);
    List<Customer> customerList = page.getContent();
    totalResults = page.getTotalElements();
    totalPages = page.getTotalPages();
    if (customerList.isEmpty()) {
      logger.error("No customer with Phone Number {}", phone);
      throw new CustomerNotFoundException("No customer with phone number " + phone,
          "Please enter in an acceptable phone number");
    } else {
      logger.debug("Returning list of customers with phone number {}", phone);
      return getAClientCustomerList(customerList);
    }
  }

  /*
   * Returns a list of ClientCustomers by address
   */

  @Override
  public List<ClientCustomer> getCustomerByAddress(String address, int offSet, int pageSize) {
    Pageable pageable = PageRequest.of(offSet, pageSize);
    Page<Customer> page = repository.getByAddress(address, pageable);
    List<Customer> customerList = page.getContent();
    totalResults = page.getTotalElements();
    totalPages = page.getTotalPages();
    if (customerList.isEmpty()) {
      logger.error("No customer with address {]", address);
      throw new CustomerNotFoundException("No customer with address " + address,
          "Please enter in an acceptable address");
    } else {
      logger.debug("Returing list of ClientCustomers with address {}", address);
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
      logger.debug("Returning List of customers with car id {}", id);
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
  public List<ClientCustomer> getCustomerByEmail(String email, int offSet, int pageSize) {
    Pageable pageable = PageRequest.of(offSet, pageSize);
    Page<Customer> page = repository.getByEmail(email, pageable);
    List<Customer> customerList = page.getContent();
    totalResults = page.getTotalElements();
    totalPages = page.getTotalPages();
    if (customerList.isEmpty()) {
      logger.error("No customer with email {}", email);
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
  public Map<String, Object> search(String name, String address, String phone, String email,
      int offSet, int pageSize) {
    Integer nameParam = 0;
    Integer addressParam = 0;
    Integer phoneParam = 0;
    Integer emailParam = 0;
    List<ClientCustomer> returnList = new ArrayList<ClientCustomer>();
    List<ClientCustomer> tempCustomer = new ArrayList<ClientCustomer>();
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
        return getResponse(getAllCustomers(offSet, pageSize), offSet, pageSize);
      case 1:
        if (nameParam == 1) {
          // Searching for all customers by specific name
          logger.debug("Searching for name {}", name);
          return getResponse(getCustomers(name, offSet, pageSize), offSet, pageSize);
        } else if (phoneParam == 1) {
          // Searching for all customers by phone number
          logger.debug("Searching for phone number {}", phone);
          return getResponse(getCustomerByPhoneNumber(phone, offSet, pageSize), offSet, pageSize);
        } else if (addressParam == 1) {
          // Searching for all customers by address
          logger.debug("Searching for address {}", address);
          return getResponse(getCustomerByAddress(address, offSet, pageSize), offSet, pageSize);
        } else if (emailParam == 1) {
          // Searching for all customers by email
          logger.debug("Searching for email {}", email);
          return getResponse(getCustomerByEmail(email, offSet, pageSize), offSet, pageSize);
        } else {
          logger.error("Should not have reached this point");
          throw new IllegalStateException();
        }
      case 2:
        if (nameParam == 1 && phoneParam == 1) {
          // Searching for all customers by specific name and phone number TODO add more try and
          // catch and stream to get distinct objects
          logger.debug("Searching for name {} and phone number {}", name, phone);
          try {
            tempCustomer.addAll(getCustomers(name, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with name {}", name);
          }
          try {
            tempCustomer.addAll(getCustomerByPhoneNumber(phone, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with phone {}", phone);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with name {} or phone number {}", name, phone);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with name or phone number entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (nameParam == 1 && addressParam == 1) {
          // Searching for all customers by specific name and address
          logger.debug("Searching for name {} and address {}", name, address);
          try {
            tempCustomer.addAll(getCustomers(name, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with Name ", name);
          }
          try {
            tempCustomer.addAll(getCustomerByAddress(address, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with Address ", address);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with name {} or address {}", name, address);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with name or address entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (nameParam == 1 && emailParam == 1) {
          // Searching for all customers by specific name and email
          logger.debug("Searching for name {} and email {}", name, email);
          try {
            tempCustomer.addAll(getCustomers(name, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with name {}", name);
          }
          try {
            tempCustomer.addAll(getCustomerByEmail(email, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with email {}", email);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with name {} or email {}", name, email);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with name or address entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (phoneParam == 1 && addressParam == 1) {
          // Searching for all customers by phone number and address
          logger.debug("Searching for phone {} and address {}", phone, address);
          try {
            tempCustomer.addAll(getCustomerByPhoneNumber(phone, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with phone number {}", phone);
          }
          try {
            tempCustomer.addAll(getCustomerByAddress(address, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with address {}", address);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with phone number {} or address {}", phone, address);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with phone number or address entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (phoneParam == 1 && emailParam == 1) {
          // Searching for all customers by phone number and email
          logger.debug("Searching for phone {} and email {}", phone, email);
          try {
            tempCustomer.addAll(getCustomerByPhoneNumber(phone, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with phone number {}", phone);
          }
          try {
            tempCustomer.addAll(getCustomerByEmail(email, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer wth email {}", email);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with phone number {} or email {}", phone, email);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with phone number or email entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (addressParam == 1 && emailParam == 1) {
          // Searching for all customers by email and address
          logger.debug("Searching for address {} and email {}", address, email);
          try {
            tempCustomer.addAll(getCustomerByAddress(address, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with address {}", address);
          }
          try {
            tempCustomer.addAll(getCustomerByEmail(email, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with email {}", email);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with address {} or email {}", address, email);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with address or email entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else {
          logger.error("Should not have reached this point");
          throw new IllegalStateException();
        }
      case 3:
        if (nameParam == 1 && phoneParam == 1 && addressParam == 1) {
          // Searching for all customers by specific name and phone number and address
          logger.debug("Searching for name {} and phone {} and address {}", name, phone, address);
          try {
            tempCustomer.addAll(getCustomers(name, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with name {}", name);
          }
          try {
            tempCustomer.addAll(getCustomerByPhoneNumber(phone, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with phone number {}", phone);
          }
          try {
            tempCustomer.addAll(getCustomerByAddress(address, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with address {}", address);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with name {} or phone number {} or address {}", name, phone,
                address);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with name, phone number or address entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (nameParam == 1 && phoneParam == 1 && emailParam == 1) {
          // Searching for all customers by specific name and phone number and email
          logger.debug("Searching for name {} and phone {} and email {}", name, phone, email);
          try {
            tempCustomer.addAll(getCustomers(name, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with name {}", name);
          }
          try {
            tempCustomer.addAll(getCustomerByPhoneNumber(phone, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with phone number {}", phone);
          }
          try {
            tempCustomer.addAll(getCustomerByEmail(email, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with email", email);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with name {} or phone number {} or eamil {}", name, phone,
                email);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with name, phone number or email entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (nameParam == 1 && emailParam == 1 && addressParam == 1) {
          // Searching for all customers by specific name and address and email
          logger.debug("Searching for name {} and email {} and address {}", name, email, address);
          try {
            tempCustomer.addAll(getCustomers(name, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with name {}", name);
          }
          try {
            tempCustomer.addAll(getCustomerByEmail(email, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No Customer with email {}", email);
          }
          try {
            tempCustomer.addAll(getCustomerByAddress(address, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with address {}", address);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with name {} or email {} or address {}", name, email,
                address);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with name, email or address entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else if (phoneParam == 1 && emailParam == 1 && addressParam == 1) {
          // Searching for all customers by email and phone number and address
          logger.debug("Searching for phone {} and email {} and address {}", phone, email, address);
          try {
            tempCustomer.addAll(getCustomerByPhoneNumber(phone, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with phone number {}", phone);
          }
          try {
            tempCustomer.addAll(getCustomerByEmail(email, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with email {}", email);
          }
          try {
            tempCustomer.addAll(getCustomerByAddress(address, offSet, pageSize));
          } catch (CustomerNotFoundException e) {
            logger.debug("No customer with address {}", address);
          }
          returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
          if (returnList == null) {
            logger.error("No customer with phone number {} or email {} or address {}", phone, email,
                address);
            throw new CustomerNotFoundException("Nothing to search",
                "No customer with phone number, email or address entered please try again");
          }
          return getResponse(returnList, offSet, pageSize);
        } else {
          logger.debug("This section should not be reached");
          throw new IllegalStateException();
        }
      case 4:
        // Searching for all customers by all search parameters
        try {
          tempCustomer.addAll(getCustomers(name, offSet, pageSize));
        } catch (CustomerNotFoundException e) {
          logger.debug("No customer with name {}", name);
        }
        try {
          tempCustomer.addAll(getCustomerByPhoneNumber(phone, offSet, pageSize));
        } catch (CustomerNotFoundException e) {
          logger.debug("No customer the phone number {}", phone);
        }
        try {
          tempCustomer.addAll(getCustomerByEmail(email, offSet, pageSize));
        } catch (CustomerNotFoundException e) {
          logger.debug("No customer with email {}", email);
        }
        try {
          tempCustomer.addAll(getCustomerByAddress(address, offSet, pageSize));
        } catch (CustomerNotFoundException e) {
          logger.debug("No customer with address {}", address);
        }
        returnList = tempCustomer.stream().distinct().collect(Collectors.toList());
        if (returnList == null) {
          logger.error("No customer with name{}, phone number {} , email {} or address {}", name,
              phone, email, address);
          throw new CustomerNotFoundException("Nothing to search",
              "No customer with name, phone number, email or address entered please try again");
        }
        return getResponse(returnList, offSet, pageSize);
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
      logger.debug("Adding a new customer to the database {}", customer);
      return repository.save(customer);
    } else {
      logger.error("Id is already present {}", id);
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
      logger.debug("Updating old customer {}", customer);
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
      logger.error("No customer to delete with Id {}", id);
      throw new CustomerNotFoundException("No customer with Id " + id,
          "Please enter in a valid ID");
    } else {
      logger.debug("Deleting record with id {}", id);
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

  @Override
  public Map<String, Object> getResponse(List<ClientCustomer> clientCustomerList, int offSet,
      int pageSize) {
    Pageable pageable = PageRequest.of(offSet, pageSize);
    Page<ClientCustomer> resultPage =
        new PageImpl<ClientCustomer>(clientCustomerList, pageable, clientCustomerList.size());
    Map<String, Object> response = new HashMap<>();
    response.put("content", resultPage.getContent());
    response.put("currant page", resultPage.getNumber());
    response.put("total results", totalResults);
    response.put("total pages", totalPages);
    return response;
  }
}
