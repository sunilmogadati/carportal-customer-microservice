package com.quintrix.carportal.customer.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.quintrix.carportal.customer.entity.Customer;

public interface CustomerRepository
    extends MongoRepository<Customer, Long>, PagingAndSortingRepository<Customer, Long> {
  @Query(value = "{'name': {$regex: /?0/, $options: 'i' }}", sort = "{ 'name' : 1 }",
      collation = "en")
  Page<Customer> getAllByName(String name, Pageable pageable);

  Page<Customer> getByPhoneNumber(String phone, Pageable pageable);

  Page<Customer> getByAddress(String address, Pageable pageable);

  Page<Customer> getByEmail(String email, Pageable pageable);
}
