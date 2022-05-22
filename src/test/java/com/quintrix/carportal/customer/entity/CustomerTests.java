package com.quintrix.carportal.customer.entity;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ActiveProfiles;
import com.quintrix.carportal.customer.repository.CustomerRepository;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
@ActiveProfiles("test")
class CustomerTests {

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
  void testPopulateFields() {
    Customer customer = new Customer();
    List<String> carsOwned = new ArrayList<>();
    carsOwned.add("1");
    carsOwned.add("2");
    carsOwned.add("3");

    // only ID and Active should be populated at this point, and Active should always be false

    Assertions.assertNotNull(customer.getId());
    Assertions.assertNull(customer.getName());
    Assertions.assertNull(customer.getPhoneNumber());
    Assertions.assertFalse(customer.isActive());
    Assertions.assertNull(customer.getOwnedCars());
    customer.setName("Jonathan Test");
    customer.setEmail("jtest@gmail.com");
    customer.setPhoneNumber("9705555555");
    customer.setActive(true);
    customer.setOwnedCars(carsOwned);

    // all fields should be populated now

    Assertions.assertNotNull(customer.getId());
    Assertions.assertNotNull(customer.getName());
    Assertions.assertNotNull(customer.getPhoneNumber());
    Assertions.assertTrue(customer.isActive());
    Assertions.assertNotNull(customer.getOwnedCars());
  }

}

