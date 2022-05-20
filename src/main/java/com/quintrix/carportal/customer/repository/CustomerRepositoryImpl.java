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
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;



@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
  private final MongoTemplate mongoTemplate;

  public CustomerRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public Optional<Customer> findById(long id) {
    Objects.requireNonNull(id);

    Customer possibleCustomerFound = mongoTemplate.findById(id, Customer.class);
    return Optional.ofNullable(possibleCustomerFound);
  }

  @Override
  public List<Customer> findAll() {
    return mongoTemplate.findAll(Customer.class);
  }

  @Override
  public List<Customer> findAllByName(String name) {
    Query query = new Query();
    query.addCriteria(Criteria.where("name").regex(name, "i"));
    return mongoTemplate.find(query, Customer.class);
  }

  @Override
  public Customer save(Customer customer) {
    Objects.requireNonNull(customer);

    if (customer.getId() < 1) {
      customer.setId(generateSequence());
    }

    Customer savedCustomer = mongoTemplate.save(customer);
    return savedCustomer;
  }

  @Override
  public void delete(Customer customer) {
    Objects.requireNonNull(customer);

    mongoTemplate.remove(customer);
  }

  @Override
  public void deleteById(long id) {
    Objects.requireNonNull(id);

    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(id));
    mongoTemplate.findAndRemove(query, Customer.class);
  }

  private long generateSequence() {
    // source: https://www.baeldung.com/spring-boot-mongodb-auto-generated-field

    final String SEQUENCE_NAME = "customers_sequence";
    final String SEQUENCE_FIELD_TO_INCREMENT = "seq";
    final int AMOUNT_TO_INCREMENT_BY = 1;
    final String SEQUENCE_ID_FIELD_NAME = "_id";

    Query queryToFindSequence = new Query();

    queryToFindSequence.addCriteria(Criteria.where(SEQUENCE_ID_FIELD_NAME).is(SEQUENCE_NAME));
    DatabaseSequence counter = mongoTemplate.findAndModify(
        queryToFindSequence,
        new Update().inc(SEQUENCE_FIELD_TO_INCREMENT, AMOUNT_TO_INCREMENT_BY),
        FindAndModifyOptions.options().returnNew(true).upsert(true),
        DatabaseSequence.class
    );

    return !Objects.isNull(counter) ? counter.getSeq() : 1;
  }

  @Document(collection = "database_sequences")
  private static class DatabaseSequence {
    // source: https://www.baeldung.com/spring-boot-mongodb-auto-generated-field

    @Id
    private String id;
    private long seq;

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public long getSeq() {
      return seq;
    }

    public void setSeq(long seq) {
      this.seq = seq;
    }
  }
}

@Document(collection = "customers")
/* NOTE: This is Customer class is only for testing */
/* TODO: this class will eventually need to be deleted and replaced with the one used for project. */
class Customer {
  @Id
  private long id;
  private String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
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
    
    final int numCustomersInDbBeforeStarting = customerRepositoryImpl.findAll().size();

    customer1 = customerRepositoryImpl.save(customer1);
    System.out.println("test 1: customer 1 should be added");
    System.out.println(customerRepositoryImpl.findAll().size() == numCustomersInDbBeforeStarting + 1);
    System.out.println(customerRepositoryImpl.findById(customer1.getId()).isPresent() == true);
    System.out.println("test 2: customer 1 should have a positive id");
    System.out.println(customerRepositoryImpl.findById(customer1.getId()).get().getId() > 0);

    customer2 = customerRepositoryImpl.save(customer2);
    System.out.println("test 3: customer 2 should be added");
    System.out.println(customerRepositoryImpl.findAll().size() == numCustomersInDbBeforeStarting + 2);
    System.out.println(customerRepositoryImpl.findById(customer2.getId()).isPresent() == true);
    System.out.println("test 4: customer 2 should have a positive id");
    System.out.println(customerRepositoryImpl.findById(customer2.getId()).get().getId() > 0);
    System.out.println("test 5: customer 1's id should be one less than customer 2's id");
    System.out.println(customer1.getId() + 1 == customer2.getId());


    final String CUSTOMER_1_UPDATED_NAME = "Customer One_updated";
    customer1.setName(CUSTOMER_1_UPDATED_NAME);
    customerRepositoryImpl.save(customer1);
    System.out.println("test 6: customer 1 should be updated");
    System.out.println(
        customerRepositoryImpl.findById(customer1.getId()).get().getName().equals(CUSTOMER_1_UPDATED_NAME)
    );

    long customerOneId = customer1.getId();

    List<Customer> customersFoundByName = customerRepositoryImpl.findAllByName(CUSTOMER_1_UPDATED_NAME);
    System.out.println("test 7: customer 1 should be found when searching by customer 1's name");
    System.out.println(customersFoundByName.size() == 1);
    System.out.println(customersFoundByName.get(0).getId() == customerOneId);


    customerRepositoryImpl.delete(customer1);
    System.out.println("test 8: the number of customers should decrease by 1 when customer customer 1 is deleted");
    System.out.println(customerRepositoryImpl.findAll().size() == numCustomersInDbBeforeStarting + 1);
    System.out.println("test 9: customer 1 should not be in database after it is deleted");
    System.out.println(customerRepositoryImpl.findById(customerOneId).isPresent() == false);
    System.out.println("test 10: customer 2 should be in database after deleting customer 1");
    System.out.println(customerRepositoryImpl.findById(customer2.getId()).isPresent());

    customerRepositoryImpl.deleteById(customer2.getId());
    System.out.println("test 11: customer 2 should not be in database after it is deleted");
    System.out.println(customerRepositoryImpl.findAll().size() == numCustomersInDbBeforeStarting);
  }

  private Customer createCustomerOne() {
    Customer customer = new Customer();
    customer.setName("Customer One");

    return customer;
  }

  private Customer createCustomerTwo() {
    Customer customer = new Customer();
    customer.setName("Customer Two");

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