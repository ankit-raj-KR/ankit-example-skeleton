package com.kroger.ankitexampleskeleton.controller;

import com.kroger.ankitexampleskeleton.config.CustomConfig;
import com.kroger.ankitexampleskeleton.model.UserDTO;
import com.kroger.ankitexampleskeleton.service.UserService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@EnableConfigurationProperties(CustomConfig.class)
public class UserController {
  private final CustomConfig customConfig;
  private final UserService userService;

  public UserController(UserService userService, CustomConfig customConfig) {
    this.userService = userService;
    this.customConfig = customConfig;
  }

  @GetMapping("/welcome")
  public Mono<ResponseEntity<String>> welcomeMessage() {
    return Mono.just(ResponseEntity.ok(customConfig.message()));
  }

  @GetMapping("/users")
  public Mono<ResponseEntity<Flux<UserDTO>>> getUsers() {
    return Mono.just(ResponseEntity.ok(userService.getAllUsers()));
  }

  @PostMapping("/create")
  public Mono<ResponseEntity<Mono<Long>>> createUser(@RequestBody UserDTO user) {
    Mono<Long> id = userService.createUser(user);
    return Mono.just(ResponseEntity.status(201).body(id));
  }

  @GetMapping("/user/{id}")
  public Mono<ResponseEntity<String>> findUserById(@PathVariable Long id) {
    return userService
        .findUserById(id)
        .map(data -> ResponseEntity.ok(data.name()))
        .defaultIfEmpty(ResponseEntity.ok("no user found"));
  }

  @PutMapping("update/user/{id}")
  public Mono<ResponseEntity<String>> updateUser(@PathVariable Long id, @RequestBody String name) {
    return userService
        .updateUser(id, name)
        .map(data -> ResponseEntity.ok(data.toString()))
        .defaultIfEmpty(ResponseEntity.ok("no user found"));
  }

  @DeleteMapping("delete/user/{id}")
  public Mono<ResponseEntity<Mono<String>>> deleteUser(@PathVariable Long id) {
    return userService.deleteUser(id).map(data -> ResponseEntity.ok(Mono.just(data)));
  }
}
