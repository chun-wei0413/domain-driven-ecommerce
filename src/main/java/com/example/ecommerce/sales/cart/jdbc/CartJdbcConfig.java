package com.example.ecommerce.sales.cart.jdbc;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
/**
 * Configuration for JDBC implementation for Cart service.
 */
@Configuration
public class CartJdbcConfig {

    @Bean
    RetrieveCartJdbc retrieveCartJdbc(JdbcTemplate jdbcTemplate) {
        return new RetrieveCartJdbc(jdbcTemplate);
    }
}
