package com.kroger.ankitexampleskeleton.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "custom")
public record CustomConfig(String message) {}
