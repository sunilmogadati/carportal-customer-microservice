package com.quintrix.carportal.customer.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * Configuration for the database that will provide a {@link MongoTemplate} instance to be used.
 */
@Configuration
public class MongoConfig {
  @Value("${database.name}")
  private String CUSTOMER_REPOSITORY_NAME;
  @Value("${database.hostWithPort}")
  private String CUSTOMER_DB_LOCATION;


  // NOTE: most of this class comes from https://www.baeldung.com/spring-data-mongodb-tutorial

  @Bean
  public MongoClient mongo() {
    ConnectionString connectionString = new ConnectionString(CUSTOMER_DB_LOCATION + CUSTOMER_REPOSITORY_NAME);
    MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
        .applyConnectionString(connectionString)
        .build();

    return MongoClients.create(mongoClientSettings);
  }

  @Bean
  public MongoTemplate mongoTemplate() throws Exception {
    return new MongoTemplate(mongo(), CUSTOMER_REPOSITORY_NAME);
  }
}