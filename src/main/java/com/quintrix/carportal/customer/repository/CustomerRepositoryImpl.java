package com.quintrix.carportal.customer.repository;

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
    Objects.requireNonNull(name);

    Query query = new Query();
    query.addCriteria(Criteria.where("name").regex("^" + name + "$", "i"));
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