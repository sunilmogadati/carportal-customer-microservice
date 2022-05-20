package com.quintrix.carportal.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;
import com.quintrix.jfs.customer.documents.Customer;
import com.quintrix.jfs.customer.models.ClientCustomer;
import com.quintrix.jfs.customer.models.GetCustomerResponse;
import com.quintrix.jfs.customer.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  CustomerRepository customerRepository;

  /*
   * @Autowired RestTemplate restTemplate;
   * 
   * 
   * @Value("${agentService.getUr2}") String agentServiceGetUr2;
   * 
   * List<Customer> customersList = new ArrayList<>();
   */

  @Override
  public List<Customer> getAllCustomers() {

    List<Customer> custList = customerRepository.findAll();

    return custList;
  }

  @Override
  public Customer addCustomer(Customer customer) {

    Customer createdCustomer = customerRepository.insert(customer);

    return createdCustomer;
  }

  @Override
  public Customer getCustomerById(Long id) {

    Iterable<Customer> cust = customerRepository.findAll();
    List<Customer> customerList = Streamable.of(cust).toList();

    Optional<Customer> customer = customerList.stream().filter(c -> c.getId().equals(id)).findAny();

    return customer.get();

  }

  @Override
  public GetCustomerResponse getCustomers(String name) {

    GetCustomerResponse getCustomerResponse = new GetCustomerResponse();

    List<Customer> custList = getAllCustomers();

    if (name != null) {

      getCustomerResponse.setAvailableCustomersList(custList.stream()
          .filter(c -> c.getName().equals(name)).map(c -> new ClientCustomer(c.getName(),
              c.getPhone(), c.getAge(), c.getActive(), c.getState()))
          .collect(Collectors.toList()));
    }

    getCustomerResponse.setAvailableCustomers("All customers are available");
    return getCustomerResponse;

  }


}
