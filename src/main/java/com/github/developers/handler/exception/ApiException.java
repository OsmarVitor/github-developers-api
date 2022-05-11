package com.github.developers.handler.exception;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiException {

  private String message;
  private int status;
  private LocalDate date;
}
