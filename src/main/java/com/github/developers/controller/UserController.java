package com.github.developers.controller;

import com.github.developers.model.User;
import com.github.developers.model.dto.LoginDTO;
import com.github.developers.model.dto.UserDTO;
import com.github.developers.model.dto.UserLoginResponseDTO;
import com.github.developers.service.UserService;
import com.github.developers.service.impl.UserDetailsServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.UUID;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@CrossOrigin("*")
@Api(value = "API for management users.")
@RequestMapping("user")
public class UserController {

  @Autowired private UserService<UserDTO> userService;

  @Autowired private UserDetailsServiceImpl userDetailsService;

  @PostMapping("/create")
  @ApiOperation(value = "Create a new user")
  ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
    User user = userService.create(userDTO);
    return ResponseEntity.created(
            ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{uuid}")
                .buildAndExpand(user.getUuid())
                .toUri())
        .build();
  }

  @PostMapping("/login")
  @ApiOperation(value = "Register a user")
  ResponseEntity<UserLoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
    return ResponseEntity.ok(userDetailsService.loginUser(loginDTO));
  }

  @GetMapping("/{id}")
  @ApiOperation(value = "Search for a user by their id")
  ResponseEntity<UserDTO> findById(@PathVariable(name = "id") UUID uuid) {
    return ResponseEntity.ok(userService.findById(uuid));
  }

  @GetMapping("find-all")
  @ApiOperation(value = "Search all users")
  ResponseEntity<Page<UserDTO>> findAll(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "15") int size) {
    return ResponseEntity.ok(userService.findAll(PageRequest.of(page, size)));
  }

  @DeleteMapping()
  @ApiOperation(value = "Delete a user")
  ResponseEntity<Void> deleteUser(@RequestBody UserDTO userDTO) {
    userService.delete(userDTO);
    return ResponseEntity.noContent().build();
  }

  @PutMapping()
  @ApiOperation(value = "Update a user")
  ResponseEntity<Void> updateUser(@RequestBody UserDTO userDTO) {
    userService.update(userDTO);
    return ResponseEntity.noContent().build();
  }
}
