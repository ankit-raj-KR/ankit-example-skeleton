package com.kroger.ankitexampleskeleton.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.kroger.ankitexampleskeleton.Repository.UserRepository;
import com.kroger.ankitexampleskeleton.model.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {
  @Autowired private UserRepository userRepository;
  @Autowired private UserService userService;

  @BeforeEach
  void initialSetup() {
    userRepository.deleteAll().block();
  }

  @Test
  void getAllUsers() {
    var initialUserList = userService.getAllUsers().collectList().block();
    assertThat(initialUserList).isNotNull();
    assertThat(initialUserList.size()).isEqualTo(0);

    var user1 = new UserDTO(null, "Ankit Raj");
    var user2 = new UserDTO(null, "Bishal Ghosh");
    var user3 = new UserDTO(null, "Aditya Rathore");

    userRepository.save(user1).block();
    userRepository.save(user2).block();
    userRepository.save(user3).block();

    var newUserList = userService.getAllUsers().collectList().block();
    assertThat(newUserList).isNotNull();
    assertThat(newUserList.size()).isEqualTo(3);

    assertThat(newUserList.get(0).name()).isEqualTo(user1.name());
  }

  @Test
  void createUser() {
    var oldUserList = userRepository.findAll().collectList().block();
    assertThat(oldUserList).isNotNull();
    var initialCount = oldUserList.size();

    var newUser = new UserDTO(null, "Ankit Raj");
    userService.createUser(newUser).block();

    var newUserList = userRepository.findAll().collectList().block();
    assertThat(newUserList).isNotNull();
    var newCount = newUserList.size();

    assertThat(newCount).isEqualTo(initialCount + 1);
  }

  @Test
  void findUserById() {
    var user = new UserDTO(null, "Ankit Raj");
    var savedUser = userRepository.save(user).block();
    var savedUserId = savedUser.id();

    var foundUser = userService.findUserById(savedUserId).block();
    assertThat(foundUser).isNotNull();
    assertThat(foundUser.name()).isEqualTo(user.name());
  }

  @Test
  void updateUser() {
    var user = new UserDTO(null, "Ankit Raj");
    var savedUser = userRepository.save(user).block();
    assertThat(savedUser).isNotNull();
    var savedUserId = savedUser.id();

    userService.updateUser(savedUserId, "Aditya Rathore").block();

    var updatedUser = userRepository.findById(savedUserId).block();
    assertThat(updatedUser).isNotNull();
    assertThat(updatedUser.name()).isEqualTo("Aditya Rathore");
  }

  @Test
  void deleteUser() {
    var user = new UserDTO(null, "Ankit Raj");
    var savedUser = userRepository.save(user).block();
    var savedUserId = savedUser.id();

    userService.deleteUser(savedUserId).block();

    var deletedUser = userRepository.findById(savedUserId).block();
    assertThat(deletedUser).isNull();
  }
}
