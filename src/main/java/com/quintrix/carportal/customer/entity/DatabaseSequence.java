package com.quintrix.carportal.customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
// The purpose of this class it to store sequences so that the customer id will be able to be auto
// incremented using a stored sequence

// NOTE: this class came from
// https://github.com/eugenp/tutorials/blob/master/persistence-modules/spring-boot-persistence-mongodb/src/main/java/com/baeldung/mongodb/models/DatabaseSequence.java
@Document
public class DatabaseSequence {
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
