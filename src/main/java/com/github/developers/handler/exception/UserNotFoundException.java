package com.github.developers.handler.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String param, String value) {
    super("User with " + param + " [ " + value + "] not found.");
  }
}
