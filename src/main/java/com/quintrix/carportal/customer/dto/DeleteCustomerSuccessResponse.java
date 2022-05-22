package com.quintrix.carportal.customer.dto;

public class DeleteCustomerSuccessResponse {
  private String message;

  public DeleteCustomerSuccessResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
