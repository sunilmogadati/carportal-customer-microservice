package com.quintrix.carportal.customer.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.quintrix.carportal.customer.entity.ClientCustomer;
import com.quintrix.carportal.customer.entity.Customer;

public interface CustomerService {

  Customer updateCustomer(Customer customer);

  String deleteCustomer(Long id);

  List<ClientCustomer> getAllCustomers(int offSet, int pageSize);

  List<ClientCustomer> getCustomers(String name, int offSet, int pageSize);

  Optional<Customer> getCustomerById(Long id);

  Customer addCustomer(Customer customer);

  List<Customer> getAllCustomersAdmin(int offSet, int pageSize);

  List<ClientCustomer> getCustomerByPhoneNumber(String phone, int offSet, int pageSize);

  List<ClientCustomer> getCustomerByAddress(String address, int offSet, int pageSize);

  List<ClientCustomer> getCustomerByCar(String id);

  Map<String, Object> search(String name, String address, String phone, String email, int offSet,
      int pageSize);

  List<ClientCustomer> getCustomerByEmail(String email, int offSet, int pageSize);

  List<ClientCustomer> getAClientCustomerList(List<Customer> customerList);

  Map<String, Object> getResponse(List<ClientCustomer> clientCustomerList, int offSet,
      int pageSize);

}
