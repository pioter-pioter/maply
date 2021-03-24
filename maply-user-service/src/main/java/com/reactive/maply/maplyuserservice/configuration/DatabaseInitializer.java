package com.reactive.maply.maplyuserservice.configuration;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

@Configuration
public class DatabaseInitializer {
    @Bean
    ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer cfi = new ConnectionFactoryInitializer();
        cfi.setConnectionFactory(connectionFactory);
        cfi.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));
        return cfi;
    }
}
