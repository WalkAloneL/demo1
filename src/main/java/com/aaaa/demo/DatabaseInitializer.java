package com.aaaa.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class DatabaseInitializer {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void init() {
        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS users (" //
                + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                + "name VARCHAR(100) NOT NULL, " //
                + "password VARCHAR(100) NOT NULL, " //
                + "createdAt BIGINT NOT NULL, " //
                + "UNIQUE (name))");

        jdbcTemplate.update("CREATE TABLE IF NOT EXISTS comments (" //
                + "id BIGINT IDENTITY NOT NULL PRIMARY KEY, " //
                + "name VARCHAR(200) NOT NULL, "//
                + "text VARCHAR(200) NOT NULL, " //
                + "createdAt BIGINT NOT NULL)");
        System.out.println("qidong");
    }
}