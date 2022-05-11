package com.github.developers.service.impl;

import com.github.developers.handler.exception.UserBadRequestException;
import com.github.developers.handler.exception.UserNotFoundException;
import com.github.developers.model.User;
import com.github.developers.model.dto.UserDTO;
import com.github.developers.repository.UserRepository;
import com.github.developers.service.UserService;
import com.github.developers.utils.ConverterDTO;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService<UserDTO> {

  @Autowired private UserRepository userRepository;

  @Override
  public User create(UserDTO dto) {
    if (userRepository.findByEmail(dto.email).isPresent())
      throw new UserBadRequestException(dto.email);
    User userToCreate = ConverterDTO.convertToUser(dto);
    return userRepository.save(userToCreate);
  }

  @Override
  public User update(UserDTO dto) {
    User user =
        userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("email", dto.email));

    if (dto.getName().isEmpty()) {
      throw new UserBadRequestException(dto.getEmail());
    }
    user.setName(dto.getName());
    return userRepository.save(user);
  }

  @Override
  public void delete(UserDTO dto) {
    User user =
        userRepository
            .findByEmail(dto.getEmail())
            .orElseThrow(() -> new UserNotFoundException("email", dto.email));
    userRepository.delete(user);
  }

  @Override
  public Page<UserDTO> findAll(Pageable pageable) {
    return userRepository.findAll(pageable).map(user -> ConverterDTO.convertToDTO(user));
  }

  @Override
  public UserDTO findByEmail(String email) {
    User user =
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("email", email));
    return ConverterDTO.convertToDTO(user);
  }

  @Override
  public UserDTO findById(UUID id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new UserNotFoundException("uuid", id.toString()));
    return ConverterDTO.convertToDTO(user);
  }
}
