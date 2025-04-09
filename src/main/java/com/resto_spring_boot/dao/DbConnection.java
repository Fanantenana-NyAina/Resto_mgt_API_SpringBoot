package com.resto_spring_boot.dao;

import org.springframework.context.annotation.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class DbConnection {

    private final String host = System.getenv("DB_HOST");
    private final int port = 5432;
    private final String database = System.getenv("DB_NAME");
    private final String user = System.getenv("DB_USER");
    private final String password = System.getenv("DB_PWD");

    private final String jdbcUrl = "jdbc:postgresql://" + host + ":" + port + "/" + database;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(jdbcUrl, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

