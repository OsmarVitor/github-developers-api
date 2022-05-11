package com.github.developers.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.developers.model.enums.Role;
import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor()
@Builder(builderMethodName = "newBuilder")
@NoArgsConstructor()
@Getter
@Setter
public class UserDTO {

  @NotNull private String name;

  @JsonProperty("birth_date")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate birthDate;

  @Email public String email;

  @NotNull
  @Size(min = 8, max = 99, message = "Password must be between 8 and 99")
  private String password;

  @NotNull private Role role;
}
