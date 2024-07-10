package com.kroger.ankitexampleskeleton.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
public record UserDTO(@Column("id") @Id Long id, @Column("name") String name) {}
