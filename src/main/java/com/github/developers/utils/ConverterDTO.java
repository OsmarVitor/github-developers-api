package com.github.developers.utils;

import com.github.developers.model.User;
import com.github.developers.model.dto.UserDTO;

public final class ConverterDTO {

  public static UserDTO convertToDTO(User user) {
    return UserDTO.newBuilder()
        .birthDate(user.getBirthDate())
        .name(user.getName())
        .email(user.getEmail())
        .password("*******")
        .role(user.getRole())
        .build();
  }

  public static User convertToUser(UserDTO userDTO) {
    return User.newBuilder()
        .name(userDTO.getName())
        .birthDate(userDTO.getBirthDate())
        .email(userDTO.getEmail())
        .password(userDTO.getPassword())
        .role(userDTO.getRole())
        .build();
  }
}
