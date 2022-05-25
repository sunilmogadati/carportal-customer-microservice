package com.quintrix.carportal.customer.events;

import java.util.Objects;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.entity.DatabaseSequence;

/*
 * The goal of this class is to allow for the customer id to be an auto-incremental field. This
 * class comes from using the following: -
 * https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-boot-persistence-
 * mongodb/src/main/java/com/baeldung/mongodb/events/UserModelListener.java -
 * https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-boot-persistence-
 * mongodb/src/main/java/com/baeldung/mongodb/services/SequenceGeneratorService.java
 */

@Component
public class CustomerEntityListener extends AbstractMongoEventListener<Customer> {
  private final MongoTemplate mongoTemplate;

  public CustomerEntityListener(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  @Override
  public void onBeforeConvert(BeforeConvertEvent<Customer> event) {
    if (event.getSource().getId() < 1) {
      event.getSource().setId(generateSequence());
    }
  }

  public long generateSequence() {
    final String SEQUENCE_NAME = "customers_sequence";
    Query query = new Query();
    query.addCriteria(Criteria.where("_id").is(SEQUENCE_NAME));
    DatabaseSequence counter = mongoTemplate.findAndModify(query, new Update().inc("seq", 1),
        FindAndModifyOptions.options().returnNew(true).upsert(true), DatabaseSequence.class);

    return !Objects.isNull(counter) ? counter.getSeq() : 1;
  }

}
