package com.quintrix.carportal.customer.repository;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;



@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
  private final MongoTemplate mongoTemplate;

  public CustomerRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Optional<Customer> findById(String id) {
    Objects.requireNonNull(id);

    Customer possibleCustomerFound = mongoTemplate.findById(id, Customer.class);
    return Optional.ofNullable(possibleCustomerFound);
  }

  @Override
  public List<Customer> findAll() {
    return mongoTemplate.findAll(Customer.class);
  }

  @Override
  public Customer save(Customer customer) {
    Objects.requireNonNull(customer);

    Customer savedCustomer = mongoTemplate.save(customer);
    return savedCustomer;
  }

  @Override
  public void delete(Customer customer) {
    Objects.requireNonNull(customer);

    mongoTemplate.remove(customer);
  }

  @Override
  public void deleteById(String id) {
    Objects.requireNonNull(id);

    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));
    mongoTemplate.findAndRemove(query, Customer.class);
  }
}

@Document(collection = "customers")
/* NOTE: This is Customer class is only for testing */
/* TODO: this class will eventually need to be deleted and replaced with the one used for project. */
class Customer {
  @Id
  private String id;
  /*
    NOTE: I made the id a string instead of an integer so that it is easier for MongoDB to
    automatically populate and manage it.
   */
  private String name;
  private String email;
  private String pass;
  private String phone;
  private String status;
  private String address;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPass() {
    return pass;
  }

  public void setPass(String pass) {
    this.pass = pass;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}

@Component
// NOTE: This Driver class is only for testing
/* TODO: this class will eventually need to be deleted and maybe replaced with actually JUnit test cases. */
class Driver implements CommandLineRunner {
  @Autowired
  private CustomerRepositoryImpl customerRepositoryImpl;
  @Override
  public void run(String... args) throws Exception {
    Customer customer1 = createCustomerOne();
    Customer customer2 = createCustomerTwo();
    
    final int NUM_CUSTOMERS_IN_DB_BEFORE_STARTING = customerRepositoryImpl.findAll().size();

    customer1 = customerRepositoryImpl.save(customer1);
    System.out.println("test 1: customer 1 should be added");
    System.out.println(customerRepositoryImpl.findAll().size() == NUM_CUSTOMERS_IN_DB_BEFORE_STARTING + 1);
    System.out.println("test 2: customer 1 should have an id");
    System.out.println(customerRepositoryImpl.findById(customer1.getId()).isPresent() == true);

    customer2 = customerRepositoryImpl.save(customer2);
    System.out.println("test 3: customer 2 should be added");
    System.out.println(customerRepositoryImpl.findAll().size() == NUM_CUSTOMERS_IN_DB_BEFORE_STARTING + 2);
    System.out.println("test 4: customer 2 should have an id");
    System.out.println(customerRepositoryImpl.findById(customer2.getId()).isPresent() == true);
    System.out.println("test 5: customer 1 should be different from customer 2");
    System.out.println(customer1.getId() != customer2.getId());


    customer1.setAddress("3333 New Street");
    customerRepositoryImpl.save(customer1);
    System.out.println("test 6: customer 1 should be updated");
    System.out.println(
        customerRepositoryImpl.findById(customer1.getId()).get().getAddress().equals("3333 New Street")
    );

    String customerOneId = customer1.getId();

    customerRepositoryImpl.delete(customer1);
    System.out.println("test 7: customer 1 should be deleted");
    System.out.println(customerRepositoryImpl.findAll().size() == NUM_CUSTOMERS_IN_DB_BEFORE_STARTING + 1);
    System.out.println("test 8: customer 1 should not be in database");
    System.out.println(customerRepositoryImpl.findById(customerOneId).isPresent() == false);
    System.out.println("test 9: customer 2 should be in database after deleting customer 1");
    System.out.println(customerRepositoryImpl.findById(customer2.getId()).isPresent());

    customerRepositoryImpl.deleteById(customer2.getId());
    System.out.println("test 10: customer 2 should be deleted");
    System.out.println(customerRepositoryImpl.findAll().size() == NUM_CUSTOMERS_IN_DB_BEFORE_STARTING);
  }

  private Customer createCustomerOne() {
    Customer customer = new Customer();
    customer.setName("Customer One");
    customer.setAddress("1111 Something Street");
    customer.setEmail("customer1@email.com");
    customer.setPhone("1234567890");
    customer.setPass("password123");
    customer.setStatus("active");

    return customer;
  }

  private Customer createCustomerTwo() {
    Customer customer = new Customer();
    customer.setName("Customer Two");
    customer.setAddress("2222 Else Street");
    customer.setEmail("customer2@email.com");
    customer.setPhone("1234567891");
    customer.setPass("passwordabc");
    customer.setStatus("inactive");

    return customer;
  }


}

@Configuration
/*
  NOTE: this class is only for testing.
  TODO: delete this class and replace it with one to be used for the project.
 */
class SimpleMongoConfig {
  private final String CUSTOMER_REPOSITORY_TEST_NAME = "customer_repository_test_quintrix";

  // NOTE: most of this class comes from https://www.baeldung.com/spring-data-mongodb-tutorial

  @Bean
  public MongoClient mongo() {
    ConnectionString connectionString = new ConnectionString(
        "mongodb://localhost:27017/" + CUSTOMER_REPOSITORY_TEST_NAME);
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .build();

    return MongoClients.create(mongoClientSettings);
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongo(), CUSTOMER_REPOSITORY_TEST_NAME);
  }
}