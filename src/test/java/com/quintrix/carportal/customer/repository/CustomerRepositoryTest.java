package com.quintrix.carportal.customer.repository;

import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.entity.DatabaseSequence;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CustomerRepositoryTest {
  @Autowired
  private CustomerRepository customerRepository;
  @Autowired
  private MongoTemplate mongoTemplate;

  @BeforeEach
  void setUp() {
    customerRepository.deleteAll();
  }

  @AfterAll
  void cleanUpAll() {
    customerRepository.deleteAll();
    mongoTemplate.remove(new Query(), DatabaseSequence.class);
  }

  @Test
  void getAllByNameShouldReturnAllCustomersWithGivenExactNameCaseInsensitive() {
    Customer customer = new Customer();
    customer.setName("Customer One");

    customerRepository.save(customer);

    // when perfect match
    Assertions.assertThat(customerRepository.getAllByName("Customer One")).hasSize(1);
    // casing should not matter
    Assertions.assertThat(customerRepository.getAllByName("cUstOmer onE")).hasSize(1);
    // containing name should work
    Assertions.assertThat(customerRepository.getAllByName("Customer On")).hasSize(1);
    Assertions.assertThat(customerRepository.getAllByName("one")).hasSize(1);

    Customer customer2 = new Customer();
    customer2.setName("Customer One");
    customerRepository.save(customer2);

    Customer customer3 = new Customer();
    customer3.setName("Customer Another");
    customerRepository.save(customer3);

    // should return all customers with name matching
    Assertions.assertThat(customerRepository.getAllByName("Customer One")).hasSize(2);
  }

  @Test
  void getAllByNameShouldSortAnyMatchesInAscendingOrderCaseInsensitive() {
    Customer customer1 = new Customer();
    customer1.setName("Customer");
    Customer customer2 = new Customer();
    customer2.setName("Customer C");
    Customer customer3 = new Customer();
    customer3.setName("Customer d");
    Customer customer4 = new Customer();
    customer4.setName("Customer B");
    Customer customer5 = new Customer();
    customer5.setName("Customer a");

    customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3, customer4, customer5));

    List<Customer> customers = customerRepository.getAllByName("Customer");

    // should return all customers with name matching
    Assertions.assertThat(customers).hasSize(5);
    Assertions.assertThat(customers.get(0).getName().equals("Customer"));
    Assertions.assertThat(customers.get(1).getName().equals("Customer a"));
    Assertions.assertThat(customers.get(2).getName().equals("Customer B"));
    Assertions.assertThat(customers.get(3).getName().equals("Customer C"));
    Assertions.assertThat(customers.get(4).getName().equals("Customer d"));
  }

  @Test
  void afterSaveCustomerIdShouldBePopulatedAndIncrementedByOne() {
    Customer customer1 = new Customer();
    customer1.setName("Customer One");

    Customer customer2 = new Customer();
    customer2.setName("Customer Two");

    Customer customer3 = new Customer();
    customer3.setName("Customer Three");

    customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));

    Assertions.assertThat(customer1.getId()).isPositive();
    Assertions.assertThat(customer1.getId()).isEqualTo(customer2.getId() - 1);
    Assertions.assertThat(customer2.getId()).isEqualTo(customer3.getId() - 1);

  }
}