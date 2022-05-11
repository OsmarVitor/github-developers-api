package com.github.developers.service;

import com.github.developers.model.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService<U> {
  User create(U dto);

  User update(U dto);

  void delete(U dto);

  Page<U> findAll(Pageable pageable);

  U findByEmail(String email);

  U findById(UUID id);
}
