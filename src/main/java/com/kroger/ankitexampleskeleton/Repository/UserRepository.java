package com.kroger.ankitexampleskeleton.Repository;

import com.kroger.ankitexampleskeleton.model.UserDTO;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserDTO, Long> {}
