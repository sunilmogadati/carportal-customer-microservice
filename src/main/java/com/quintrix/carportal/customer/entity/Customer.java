package com.quintrix.carportal.customer.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Table(name = "CUSTOMER")
public class Customer {

  @Id
  private long id;

  @Column(name = "NAME")
  private String name;

  @Column(name = "EMAIL")
  private String email;

  @Column(name = "PHONE_NUMBER")
  private String phoneNumber;

  @Column(name = "ACTIVE")
  private boolean active;

  @Column(name = "ADDRESS")
  private String address;

  @Column(name = "OWNED_CARS")
  private List<String> ownedCars;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public List<String> getOwnedCars() {
    return ownedCars;
  }

  public void setOwnedCars(List<String> ownedCars) {
    this.ownedCars = ownedCars;
  }

}

