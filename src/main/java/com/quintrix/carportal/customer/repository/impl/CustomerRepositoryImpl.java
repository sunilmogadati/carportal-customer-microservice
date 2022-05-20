package com.quintrix.carportal.customer.repository.impl;

import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.repository.CustomerRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;


/**
 * The implementation for the repository layer that is responsible for interacting
 * with the database.
 */
@Repository
public class CustomerRepositoryImpl implements CustomerRepository {
  private final MongoTemplate mongoTemplate;

  public CustomerRepositoryImpl(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  /**
   * gets customer from id
   *
   * @param id customer id
   * @return An Optional that is empty is returned if there is no customer or an Optional containing
   * the customer is returned if the customer was found in the database.
   */
  @Override
  public Optional<Customer> findById(long id) {
    Objects.requireNonNull(id);

    Customer possibleCustomerFound = mongoTemplate.findById(id, Customer.class);
    return Optional.ofNullable(possibleCustomerFound);
  }

  /**
   * gets all customers from the database
   *
   * @return A list of all the customers from the database is returned.
   */
  @Override
  public List<Customer> findAll() {
    return mongoTemplate.findAll(Customer.class);
  }

  /**
   * gets all customers from the database that has a given name;
   * NOTE: Casing does not matter.
   *
   * @param name customer name
   * @return A list of all the customers from the database that has the name given is returned.
   */
  @Override
  public List<Customer> findAllByName(String name) {
    Objects.requireNonNull(name);

    Query query = new Query();
    query.addCriteria(Criteria.where("name").regex("^" + name + "$", "i"));
    return mongoTemplate.find(query, Customer.class);
  }

  /**
   * saves a customer to the database
   *
   * @param customer customer to be saved
   * @return the customer saved from the database; if the customer is new the customer id will be
   * given a value from the database.
   */
  @Override
  public Customer save(Customer customer) {
    Objects.requireNonNull(customer);

    if (customer.getId() < 1) {
      customer.setId(generateSequence());
    }

    Customer savedCustomer = mongoTemplate.save(customer);
    return savedCustomer;
  }

  /**
   * deletes a customer
   *
   * @param customer customer to be deleted
   */
  @Override
  public void delete(Customer customer) {
    Objects.requireNonNull(customer);

    mongoTemplate.remove(customer);
  }

  /**
   * deletes a customer by a given customer id
   *
   * @param id the id of the customer to delete
   */
  @Override
  public void deleteById(long id) {
    Objects.requireNonNull(id);

    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(id));
    mongoTemplate.findAndRemove(query, Customer.class);
  }

  /**
   * generates a sequence so that the customer id can be auto incremented
   *
   * @return the next number in the sequence; the sequence will begin at 1
   */
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

  /**
   *  The purpose of this class is to store a sequence so that the customer id is able to be auto
   *  incremented.
   */
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