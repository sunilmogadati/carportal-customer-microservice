package com.quintrix.carportal.customer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class CustomerUser {

  @Id
  private Long id;

  // temporary: would associate customer from Customer noSQL table to this entity
  @Column(name = "CUSTOMER_ID")
  private Long customerId;

  @Column(name = "USERNAME")
  private String username;

  @Column(name = "PASSWORD")
  private String password;

  // temporary: role would be admin or customer
  @Column(name = "ROLE")
  private String role;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getCustomerId() {
    return customerId;
  }

  public void setCustomerId(Long customerId) {
    this.customerId = customerId;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  @Override
  public String toString() {
    return "CustomerUser [id=" + id + ", username=" + username + ", password=" + password
        + ", role=" + role + "]";
  }

}
