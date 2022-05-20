package com.quintrix.carportal.customer.repository;

import static org.junit.jupiter.api.Assertions.*;

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
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
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

  @BeforeAll
  void setUpAll() {
    System.out.println("name: " + mongoTemplate.getDb().getName());
  }
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
    customer1 = underTest.save(customer1);
    Assertions.assertThat(underTest.findAll()).hasSize(1);
    Assertions.assertThat(underTest.findById(customer1.getId())).isPresent();
    Assertions.assertThat(underTest.findById(customer1.getId()).get().getId()).isPositive();

    Customer customer2 = new Customer();
    customer2.setName("Customer Two");

    customer2 = underTest.save(customer2);
    Assertions.assertThat(underTest.findAll()).hasSize(2);
    Assertions.assertThat(underTest.findById(customer2.getId())).isPresent();
    Assertions.assertThat(underTest.findById(customer2.getId()).get().getId()).isPositive();

    Assertions.assertThat(customer1.getId()).isEqualTo(customer2.getId() - 1);
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