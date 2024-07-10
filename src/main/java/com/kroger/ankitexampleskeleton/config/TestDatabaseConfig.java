package com.kroger.ankitexampleskeleton.config;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class TestDatabaseConfig {
  @Bean
  public ConnectionFactoryInitializer connectionFactoryInitializer(
      ConnectionFactory connectionFactory) {
    ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
    initializer.setConnectionFactory(connectionFactory);

    CompositeDatabasePopulator compositeDatabasePopulator = new CompositeDatabasePopulator();
    compositeDatabasePopulator.addPopulators(
        new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
    initializer.setDatabasePopulator(compositeDatabasePopulator);
    return initializer;
  }
}
