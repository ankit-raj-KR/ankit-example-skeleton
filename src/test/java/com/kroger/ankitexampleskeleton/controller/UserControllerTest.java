package com.kroger.ankitexampleskeleton.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.kroger.ankitexampleskeleton.Repository.UserRepository;
import com.kroger.ankitexampleskeleton.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

  @Autowired private WebTestClient webTestClient;
  @Autowired private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    userRepository.deleteAll().block();
  }

  @Test
  void welcomeMessage() {
    webTestClient.get().uri("/welcome").exchange().expectStatus().isOk().expectBody(String.class);
  }

  @Test
  void getUsers() {
    var newUser = new UserDTO(null, "Ankit Raj");
    userRepository.save(newUser).block();

    webTestClient
        .get()
        .uri("/users")
        .exchange()
        .expectStatus()
        .isOk()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(UserDTO.class)
        .consumeWith(
            response -> {
              var usersList = response.getResponseBody();
              assertThat(usersList).isNotNull();
              assertThat(usersList.size()).isEqualTo(1);
              assertThat(usersList.get(0).name()).isEqualTo("Ankit Raj");
            });
  }

  @Test
  void createUser() {
    var newUser = new UserDTO(null, "Ankit Raj");

    webTestClient
        .post()
        .uri("/create")
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(newUser), UserDTO.class)
        .exchange()
        .expectStatus()
        .isCreated()
        .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
        .expectBody(Integer.class);

    var userList = userRepository.findAll().collectList().block();
    var newSize = userList.size();
    var user = userList.get(newSize - 1);
    assertThat(user.name()).isEqualTo("Ankit Raj");
  }

  @Test
  void findUserById() {

    var newUser = new UserDTO(null, "Ankit Raj");
    var newUserId = userRepository.save(newUser).block().id();

    webTestClient
        .get()
        .uri("/user/" + newUserId)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class)
        .isEqualTo("Ankit Raj");
  }

  @Test
  void updateUser() {

    var newUser = new UserDTO(null, "Ankit Raj");
    var newUserId = userRepository.save(newUser).block().id();

    webTestClient
        .put()
        .uri("update/user/" + newUserId)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just("Ankit Raj"), String.class)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class);

    var userList = userRepository.findAll().collectList().block();
    var user = userList.get(0);
    assertThat(user.name()).isEqualTo("Ankit Raj");
  }

  @Test
  void deleteUser() {
    var newUser = new UserDTO(null, "Ankit Raj");
    var newUserId = userRepository.save(newUser).block().id();

    webTestClient
        .delete()
        .uri("/delete/user/" + newUserId)
        .exchange()
        .expectStatus()
        .isOk()
        .expectBody(String.class);

    var userList = userRepository.findAll().collectList().block();
    assertThat(userList).isEmpty();
  }
}
