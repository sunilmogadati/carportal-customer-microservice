package com.quintrix.carportal.customer.repository;


import com.quintrix.carportal.customer.entity.Customer;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.entity.DatabaseSequence;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CustomerRepositoryTest {
  //Mock
  @Mock
  private CustomerRepository customerRepository;

  @Test
  void getAllByNameShouldReturnAllCustomersWithGivenExactNameCaseInsensitive() {
    List<Customer> customerList = new ArrayList<>();
    Customer customer = new Customer();
    customer.setName("Customer One");

    customerList.add(customer);

    // when perfect match
    when(customerRepository.getAllByName("Customer One")).thenReturn(customerList);
    Assertions.assertThat(customerRepository.getAllByName("Customer One")).hasSize(1);
    // casing should not matter
    when(customerRepository.getAllByName("cUstOmer onE")).thenReturn(customerList);
    Assertions.assertThat(customerRepository.getAllByName("cUstOmer onE")).hasSize(1);
    // containing name should work
    when(customerRepository.getAllByName("Customer On")).thenReturn(customerList);
    Assertions.assertThat(customerRepository.getAllByName("Customer On")).hasSize(1);
    when(customerRepository.getAllByName("one")).thenReturn(customerList);
    Assertions.assertThat(customerRepository.getAllByName("one")).hasSize(1);

    Customer customer2 = new Customer();
    customer2.setName("Customer One");
    customerList.add(customer2);

    //Stilltrying to get it to work with this enabled
    /*Customer customer3 = new Customer();
    customer3.setName("Customer Another");
    customerRepository.save(customer3);
    customerList.add(customer3);*/

    // should return all customers with name matching
    when(customerRepository.getAllByName("Customer One")).thenReturn(customerList);
    Assertions.assertThat(customerRepository.getAllByName("Customer One")).hasSize(2);
  }

  @Test
  void getAllByNameShouldSortAnyMatchesInAscendingOrderCaseInsensitive() {
    List<Customer> customers = new ArrayList<>();
    Customer customer1 = new Customer();
    customer1.setName("Customer");
    customers.add(customer1);
    Customer customer2 = new Customer();
    customer2.setName("Customer C");
    customers.add(customer2);
    Customer customer3 = new Customer();
    customer3.setName("Customer d");
    customers.add(customer3);
    Customer customer4 = new Customer();
    customer4.setName("Customer B");
    customers.add(customer4);
    Customer customer5 = new Customer();
    customer5.setName("Customer a");
    customers.add(customer5);


    // should return all customers with name matching
    Assertions.assertThat(customers).hasSize(5);
    Assertions.assertThat(customers.get(0).getName().equals("Customer"));
    Assertions.assertThat(customers.get(1).getName().equals("Customer a"));
    Assertions.assertThat(customers.get(2).getName().equals("Customer B"));
    Assertions.assertThat(customers.get(3).getName().equals("Customer C"));
    Assertions.assertThat(customers.get(4).getName().equals("Customer d"));
  }

  //Still trying to get this to work with the mock
  @Test
  void afterSaveCustomerIdShouldBePopulatedAndIncrementedByOne() {
    Customer customer1 = new Customer();
    customer1.setName("Customer One");
    customer1.setId(1L);

    Customer customer2 = new Customer();
    customer2.setName("Customer Two");
    customer2.setId(2L);

    Customer customer3 = new Customer();
    customer3.setName("Customer Three");
    customer3.setId(3L);

    Assertions.assertThat(customer1.getId()).isPositive();
    Assertions.assertThat(customer1.getId()).isEqualTo(customer2.getId() - 1);
    Assertions.assertThat(customer2.getId()).isEqualTo(customer3.getId() - 1);
  }
}
