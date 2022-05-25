package com.quintrix.carportal.customer.service;

import java.util.List;
import java.util.Optional;
import com.quintrix.carportal.customer.entity.ClientCustomer;
import com.quintrix.carportal.customer.entity.Customer;

public interface CustomerService {

  Customer updateCustomer(Customer customer);

  String deleteCustomer(Long id);

  List<ClientCustomer> getAllCustomers();

  <T> List<T> getCustomers(String name);

  Optional<Customer> getCustomerById(Long id);

  Customer addCustomer(Customer customer);

  List<Customer> getAllCustomersAdmin();

  List<ClientCustomer> getCustomerByPhoneNumber(String phone);

  List<ClientCustomer> getCustomerByAddress(String address);

  List<ClientCustomer> getCustomerByCar(String id);

  List<ClientCustomer> search(String name, String address, String phone, String email);

  List<ClientCustomer> getCustomerByEmail(String email);

  List<ClientCustomer> getAClientCustomerList(List<Customer> customerList);
}
