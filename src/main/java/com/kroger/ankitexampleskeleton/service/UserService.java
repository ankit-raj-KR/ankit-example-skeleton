package com.kroger.ankitexampleskeleton.service;

import com.kroger.ankitexampleskeleton.Repository.UserRepository;
import com.kroger.ankitexampleskeleton.model.UserDTO;
import java.util.logging.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
  private final Logger logger = Logger.getLogger(UserService.class.getName());
  private final UserRepository userRepository;

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public Flux<UserDTO> getAllUsers() {
    return userRepository
        .findAll()
        .onErrorContinue(
            (throwable, user) -> {
              logger.warning("error occurred on " + user + ". Error: " + throwable.getMessage());
            });
  }

  public Mono<Long> createUser(UserDTO userDTO) {
    Mono<UserDTO> newUser = userRepository.save(userDTO);
    return newUser
        .map(UserDTO::id)
        .doOnError(
            error -> {
              logger.warning("error occurred while creating an user: " + error.getMessage());
            })
        .onErrorResume(error -> Mono.empty());
  }

  public Mono<UserDTO> findUserById(Long id) {
    return userRepository
        .findById(id)
        .doOnError(
            error -> {
              logger.warning("error occurred in fetching user by id: " + error.getMessage());
            })
        .onErrorResume(error -> Mono.empty());
  }

  public Mono<Long> updateUser(Long id, String name) {
    return userRepository
        .findById(id)
        .flatMap(
            user -> {
              UserDTO updatedUser = new UserDTO(id, name);
              return userRepository.save(updatedUser);
            })
        .map(UserDTO::id)
        .doOnError(
            error -> {
              logger.warning("error occurred in updating user: " + error.getMessage());
            })
        .onErrorResume(error -> Mono.empty());
  }

  public Mono<String> deleteUser(Long id) {
    return userRepository
        .findById(id)
        .flatMap(
            user -> {
              return userRepository.delete(user).then(Mono.just("deleted successfully"));
            })
        .doOnError(
            error -> {
              logger.warning("error occurred in deleting user: " + error.getMessage());
            })
        .onErrorResume(error -> Mono.empty());
  }
}
