package com.github.developers.model;

import com.github.developers.model.enums.Role;
import java.time.LocalDate;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
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
@Entity(name = "users")
public class User extends BaseEntity {

  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID uuid;

  @NotNull(message = "NAME cannot be null")
  @Column(name = "name")
  private String name;

  @Column(name = "birth_date")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @NotNull(message = "BIRTHDATE cannot be null")
  private LocalDate birthDate;

  @Column(name = "email", unique = true)
  @NotNull(message = "IDENTIFIER cannot be null")
  public String email;

  @Column(name = "role")
  @Enumerated(EnumType.STRING)
  private Role role;

  @Column(name = "password")
  @NotNull(message = "PASSWORD cannot be null")
  private String password;
}
