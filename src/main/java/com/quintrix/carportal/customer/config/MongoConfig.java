package com.quintrix.carportal.customer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {

    private final String CUSTOMER_REPOSITORY_TEST_NAME = "customer";
    @Bean
    public MongoClient mongo() {
	ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/" + CUSTOMER_REPOSITORY_TEST_NAME);
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
