package com.quintrix.carportal.customer.repository;

import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.entity.DatabaseSequence;
import java.util.Arrays;
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
    // should not be containing
    Assertions.assertThat(customerRepository.getAllByName("Customer On")).isEmpty();
    Assertions.assertThat(customerRepository.getAllByName("One")).isEmpty();

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