package com.github.developers.controller.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.github.developers.model.User;
import com.github.developers.model.dto.UserDTO;
import com.github.developers.model.enums.Role;
import com.github.developers.repository.UserRepository;
import com.github.developers.service.UserService;
import com.google.gson.Gson;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
public class UserControllerTest {

  private static final Gson GSON = new Gson();

  protected MockMvc mockMvc;

  @Autowired private WebApplicationContext wac;

  @Autowired private UserService<UserDTO> userUserService;

  @Autowired private UserRepository userRepository;

  @Before
  public void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  }

  @Test
  @Transactional
  public void createUser() throws Exception {
    mockMvc
        .perform(
            post("/user/create")
                .content(
                    "{\"name\":\"Osmar\",\"birth_date\":\"1997-11-07\",\"email\":\"my-email@email.com\",\"password\":\"12345678\",\"role\":\"ADMIN\"}\n")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());
  }

  @Test
  @Transactional
  public void loginUser() throws Exception {
    UserDTO userDTO =
        UserDTO.newBuilder()
            .name("Osmar")
            .email("my-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build();
    User user = userUserService.create(userDTO);

    assertNotNull(user.getUuid());
    assertEquals(user.getName(), userDTO.getName());
    assertEquals(user.getEmail(), userDTO.getEmail());
    assertEquals(user.getPassword(), userDTO.getPassword());
    assertEquals(user.getRole(), userDTO.getRole());

    String responseOfLogin =
        mockMvc
            .perform(
                post("/user/login")
                    .content(GSON.toJson(user))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("tokenProvider").value("Bearer"))
            .andReturn()
            .getResponse()
            .getContentAsString();

    assertTrue(responseOfLogin.contains("token"));
  }

  @Test
  @Transactional
  public void getAllUsers() throws Exception {
    List<UserDTO> userDTOList = new ArrayList<>();

    userDTOList.add(
        UserDTO.newBuilder()
            .name("User One")
            .email("one-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build());
    userDTOList.add(
        UserDTO.newBuilder()
            .name("User Two")
            .email("two-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build());
    userDTOList.add(
        UserDTO.newBuilder()
            .name("User Three")
            .email("trhee-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build());
    userDTOList.add(
        UserDTO.newBuilder()
            .name("User Four")
            .email("four-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build());

    userDTOList.forEach(userDTO -> userUserService.create(userDTO));

    UserDTO userDTO =
        UserDTO.newBuilder()
            .name("Osmar")
            .email("my-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build();
    User user = userUserService.create(userDTO);

    String tokenOfUser =
        mockMvc
            .perform(
                post("/user/login")
                    .content(GSON.toJson(user))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString()
            .substring(10, 208);

    mockMvc
        .perform(
            get("/user/find-all")
                .header("Authentication", "Bearer " + tokenOfUser)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content.[0].name").value(userDTOList.get(0).getName()))
        .andExpect(jsonPath("$.content.[1].name").value(userDTOList.get(1).getName()))
        .andExpect(jsonPath("$.content.[2].name").value(userDTOList.get(2).getName()))
        .andExpect(jsonPath("$.content.[3].name").value(userDTOList.get(3).getName()));
  }

  @Test
  @Transactional
  public void findById() throws Exception {
    UserDTO userDTO =
        UserDTO.newBuilder()
            .name("Osmar")
            .email("my-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build();
    User user = userUserService.create(userDTO);

    String tokenOfUser =
        mockMvc
            .perform(
                post("/user/login")
                    .content(GSON.toJson(user))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString()
            .substring(10, 208);

    mockMvc
        .perform(
            get("/user/" + user.getUuid().toString())
                .header("Authentication", "Bearer " + tokenOfUser)
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value(user.getName()));
  }

  @Test
  @Transactional
  public void updateUser() throws Exception {
    UserDTO userDTO =
        UserDTO.newBuilder()
            .name("Osmar")
            .email("my-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build();
    User user = userUserService.create(userDTO);

    String tokenOfUser =
        mockMvc
            .perform(
                post("/user/login")
                    .content(GSON.toJson(user))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString()
            .substring(10, 208);

    mockMvc
        .perform(
            put("/user")
                .header("Authentication", "Bearer " + tokenOfUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    "{\"email\": \"my-email@email.com\",\n"
                        + " \"name\": \"Altered Name\"\n"
                        + "}"))
        .andExpect(status().isNoContent());

    assertEquals(userRepository.findById(user.getUuid()).get().getName(), "Altered Name");
  }

  @Test
  @Transactional
  public void deleteUser() throws Exception {
    UserDTO userDTO =
        UserDTO.newBuilder()
            .name("Osmar")
            .email("my-email@email.com")
            .password("123456789")
            .birthDate(LocalDate.now())
            .role(Role.ADMIN)
            .build();

    User user = userUserService.create(userDTO);

    String tokenOfUser =
        mockMvc
            .perform(
                post("/user/login")
                    .content(GSON.toJson(user))
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn()
            .getResponse()
            .getContentAsString()
            .substring(10, 208);

    mockMvc
        .perform(
            delete("/user")
                .header("Authentication", "Bearer " + tokenOfUser)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"my-email@email.com\"\n" + "}"))
        .andExpect(status().isNoContent());

    assertEquals(userRepository.findAll().size(), 0);
  }
}
