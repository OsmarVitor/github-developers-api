package com.github.developers.handler.exception;

public class UserBadRequestException extends RuntimeException {

  public UserBadRequestException(String email) {
    super("User with email [" + email + "] already exists.");
  }
}
