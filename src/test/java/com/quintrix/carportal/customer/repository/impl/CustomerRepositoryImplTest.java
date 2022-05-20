package com.quintrix.carportal.customer.repository.impl;

import com.quintrix.carportal.customer.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CustomerRepositoryImplTest {
  @Autowired
  private CustomerRepositoryImpl underTest;
  @Autowired
  private MongoTemplate mongoTemplate;
  private Customer customer1;

  @BeforeEach
  void setUp() {
    mongoTemplate.remove(new Query(), Customer.class);
    customer1 = new Customer();
    customer1.setName("Customer One");
  }

  @AfterAll
  void cleanUpAll() {
    mongoTemplate.getDb().drop();
  }

  @Test
  void findById() {
    underTest.save(customer1);

    Customer customer2 = new Customer();
    customer2.setName("Customer Two");
    underTest.save(customer2);

    Optional<Customer> customer = underTest.findById(customer1.getId());

    Assertions.assertThat(customer).isPresent();
    Assertions.assertThat(customer.get().getId()).isEqualTo(customer1.getId());
  }

  @Test
  void findAll() {
    underTest.save(customer1);
    Customer customer2 = new Customer();
    customer2.setName("Customer Two");
    underTest.save(customer2);

    List<Customer> customers = underTest.findAll();

    Assertions.assertThat(customers).hasSize(2);
  }

  @Test
  void findAllByName() {
    underTest.save(customer1);

    Customer customer2 = new Customer();
    customer2.setName("Hello World");
    underTest.save(customer2);
    Customer customer3 = new Customer();
    customer3.setName("Hello World");
    underTest.save(customer3);

    List<Customer> customers = underTest.findAllByName("Hello World");

    Assertions.assertThat(customers).hasSize(2);
  }

  @Test
  void save() {
    customer1.setAddress("1234 Test Street");
    customer1.setEmail("user1@email.com");
    customer1.setActive(true);
    customer1.setPassword("password123");
    customer1.setPhoneNumber("1234567890");

    customer1 = underTest.save(customer1);
    Assertions.assertThat(underTest.findAll()).hasSize(1);
    Assertions.assertThat(underTest.findById(customer1.getId())).isPresent();

    customer1 = underTest.findById(this.customer1.getId()).get();
    Assertions.assertThat(customer1.getId()).isPositive();
    Assertions.assertThat(customer1.getName()).isEqualTo("Customer One");
    Assertions.assertThat(customer1.getEmail()).isEqualTo("user1@email.com");
    Assertions.assertThat(customer1.getAddress()).isEqualTo("1234 Test Street");
    Assertions.assertThat(customer1.getPassword()).isEqualTo("password123");
    Assertions.assertThat(customer1.getPhoneNumber()).isEqualTo("1234567890");
    Assertions.assertThat(customer1.isActive()).isTrue();


    Customer customer2 = new Customer();
    customer2.setName("Customer Two");

    customer2 = underTest.save(customer2);
    Assertions.assertThat(underTest.findAll()).hasSize(2);
    Assertions.assertThat(underTest.findById(customer2.getId())).isPresent();
    Assertions.assertThat(underTest.findById(customer2.getId()).get().getId()).isPositive();

    // checking to make sure customer 2's id was auto incremented by one compared to customer 1's id
    Assertions.assertThat(this.customer1.getId()).isEqualTo(customer2.getId() - 1);
  }

  @Test
  void delete() {
    customer1 = underTest.save(customer1);

    underTest.delete(customer1);

    Assertions.assertThat(underTest.findAll()).hasSize(0);
  }

  @Test
  void deleteById() {
    customer1 = underTest.save(customer1);

    underTest.deleteById(customer1.getId());

    Assertions.assertThat(underTest.findAll()).hasSize(0);
  }
}