package com.quintrix.carportal.customer.controller;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.quintrix.carportal.customer.dto.DeleteCustomerSuccessResponse;
import com.quintrix.carportal.customer.entity.Customer;
import com.quintrix.carportal.customer.service.CustomerService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@OpenAPIDefinition(info = @Info(title = "The Customer API for Place4Cars"))
@Tag(name = "Customer API", description = "Create, retrieve, update, and delete customers.")
@RestController
public class CustomerController {

  private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

  @Autowired
  CustomerService customerService;

  /* #################### Retrieve all customers ###################### */
  /*
   * @Operation(summary = "Retrieve all existing customers.")
   * 
   * @ApiResponse(responseCode = "200", content = @Content(mediaType =
   * MediaType.APPLICATION_JSON_VALUE, array = @ArraySchema(schema = @Schema(implementation =
   * Customer.class))))
   * 
   * @RequestMapping(method = RequestMethod.GET, value = "/customers/admin") List<Customer>
   * getAllCustomers() {
   * 
   * return customerService.getAllCustomersAdmin(); }
   */

  /* ################### Retrieve all customers by name ################# */
  @Operation(summary = "Retrieve all existing customers that have the given name.")
  @ApiResponse(responseCode = "200",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          array = @ArraySchema(schema = @Schema(implementation = Customer.class))))
  // Retrieves complete customer information omitting the customer id.
  @RequestMapping(method = RequestMethod.GET, value = "/customer")
  <T> List<T> getCustomers(
      @Parameter(description = "The name to search for customers with") @RequestParam(name = "name",
          required = false) String name) {

    logger.debug("Request: Searrch records for name: {}", name);
    return customerService.getCustomers(name);
  }

  /* ################### Retrieve a customer by id ####################### */
  @Operation(summary = "Retrieve an existing customer that has the given customer id.")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = Customer.class))),
      @ApiResponse(responseCode = "404", content = @Content())})
  @RequestMapping(method = RequestMethod.GET, value = "/customer/{id}")
  public Optional<Customer> getCustomerDetails(
      @Parameter(description = "The id of customer to retrieve") @PathVariable("id") Long id) {

    logger.debug("Request: Called getCustomerById Controller {}", id);
    return customerService.getCustomerById(id);

  }

  /* ################### Add a customer ################## */
  @Operation(summary = "Add a customer using the provided customer's information.")
  @ApiResponse(responseCode = "200",
      content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
          schema = @Schema(implementation = Customer.class)))
  @RequestMapping(method = RequestMethod.POST, value = "/customer")
  Customer addCustomer(@io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "The customer's information to add") @RequestBody Customer customer) {

    logger.debug("Request: Added new customer: {}", customer);
    return customerService.addCustomer(customer);
  }

  /* ######################## Update a customer ################### */
  @Operation(
      summary = "Update an existing customer that has the given customer id with the updated "
          + "provided customer's information.")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = Customer.class))),
      @ApiResponse(responseCode = "404", content = @Content())})
  @PutMapping("/customer")
  public Customer updateCustomerInformation(@io.swagger.v3.oas.annotations.parameters.RequestBody(
      description = "The customer's updated information") @RequestBody Customer customer) {
    logger.debug("Request: update information Customer {}", customer);
    return customerService.updateCustomer(customer);
  }

  /* ################### Delete a customer ################### */
  @Operation(summary = "Delete an existing customer that has the given customer id.")
  @ApiResponses({
      @ApiResponse(responseCode = "200",
          content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
              schema = @Schema(implementation = DeleteCustomerSuccessResponse.class))),
      @ApiResponse(responseCode = "404", content = @Content())})
  @DeleteMapping("/customer/{id}")
  public DeleteCustomerSuccessResponse deleteCustomerById(
      @Parameter(description = "The id of customer to delete") @PathVariable Long id) {
    logger.debug("Request: delete Customer with id {}", id);
    return new DeleteCustomerSuccessResponse(customerService.deleteCustomer(id));
  }

  // Getters and Setters
  public CustomerService getCustomerService() {
    return customerService;
  }

  public void setCustomerService(CustomerService customerService) {
    this.customerService = customerService;
  }


  /*
   * 
   * TEST API: shows information of current authorized user
   * 
   *
   */
  @RequestMapping("/user")
  @ResponseBody
  public Principal user(Principal principal) {
    return principal;
  }

}
