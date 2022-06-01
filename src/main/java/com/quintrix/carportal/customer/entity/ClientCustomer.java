package com.quintrix.carportal.customer.entity;

import java.util.List;

public class ClientCustomer {

  private String name;

  private String email;

  private String phoneNumber;

  private String address;

  private List<String> ownedCars;



  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    ClientCustomer other = (ClientCustomer) obj;
    if (email == null) {
      if (other.email != null)
        return false;
    } else if (!email.equals(other.email))
      return false;
    return true;
  }

  public ClientCustomer(String name, String email, String phoneNumber, String address,
      List<String> ownedCars) {
    super();
    this.name = name;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.address = address;
    this.ownedCars = ownedCars;
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
