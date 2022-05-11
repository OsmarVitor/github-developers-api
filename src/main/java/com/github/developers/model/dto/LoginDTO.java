package com.github.developers.model.dto;

import com.github.developers.model.enums.Role;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "newBuilder")
@Getter
@Setter
public class LoginDTO {

  @Email private String email;

  @NotBlank private String password;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;
}
