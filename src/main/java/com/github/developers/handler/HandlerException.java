package com.github.developers.handler;

import com.github.developers.handler.exception.ApiException;
import com.github.developers.handler.exception.UserBadRequestException;
import com.github.developers.handler.exception.UserNotFoundException;

import java.time.LocalDate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {

  private static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
  private static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
  private static final HttpStatus UNAUTHORIZED = HttpStatus.UNAUTHORIZED;
  private static final HttpStatus FORBIDDEN = HttpStatus.FORBIDDEN;

  @ExceptionHandler(UserBadRequestException.class)
  public ResponseEntity<ApiException> handlerBadRequestException(
      UserBadRequestException exception) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            createResponseException(exception.getMessage(), BAD_REQUEST.value(), LocalDate.now()));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<ApiException> handlerNotFoundException(UserNotFoundException exception) {
    return ResponseEntity.status(UNAUTHORIZED)
        .body(
            createResponseException(exception.getMessage(), UNAUTHORIZED.value(), LocalDate.now()));
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiException> badCredentialsException(BadCredentialsException exception) {
    return ResponseEntity.status(FORBIDDEN)
        .body(createResponseException(exception.getMessage(), FORBIDDEN.value(), LocalDate.now()));
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiException> handlerAccessDeniedException(
      AccessDeniedException exception) {
    return ResponseEntity.status(NOT_FOUND)
        .body(createResponseException(exception.getMessage(), NOT_FOUND.value(), LocalDate.now()));
  }

  private ApiException createResponseException(String message, int statusCode, LocalDate date) {
    return new ApiException(message, statusCode, date);
  }
}
