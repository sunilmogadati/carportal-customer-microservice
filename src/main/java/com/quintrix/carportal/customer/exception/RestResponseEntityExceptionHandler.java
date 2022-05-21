package com.quintrix.carportal.customer.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = {IllegalArgumentException.class, IllegalStateException.class})
  protected ResponseEntity<Object> handleConflict(RuntimeException ex, WebRequest request) {
    String bodyOfResponse = "This should be application specific";

    Error error = new Error();
    error.setCustomMessage("This came from IllegalStateException or IllegalArguementException");;
    error.setMessage(ex.getMessage());
    error.setHttpStatusCode(HttpStatus.CONFLICT.value());
    return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.CONFLICT, request);
  }

  @ExceptionHandler({CustomerNotFoundException.class})
  public ResponseEntity<Object> handleException2(Exception ex, WebRequest request) {
    HttpHeaders headers = new HttpHeaders();

    if (ex instanceof CustomerNotFoundException) {
      HttpStatus status = HttpStatus.NOT_FOUND;
      CustomerNotFoundException unfe = (CustomerNotFoundException) ex;

      Error error = new Error();
      error.setCustomMessage("Please use a different Id");
      error.setMessage(((CustomerNotFoundException) ex).displayMessage);
      error.setHttpStatusCode(HttpStatus.NOT_FOUND.value());

      return handleExceptionInternal(ex, error, new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }
    return null;

  }
}
