package com.aaaa.demo.service;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.aaaa.demo.entity.User;

@Component
public class UserService {
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<User> userRowMapper = new BeanPropertyRowMapper<>(User.class);

    public User getUserById(long id) {
        return jdbcTemplate.queryForObject("SELECT id,name,password,createdAt FROM users WHERE id = ?", userRowMapper, new Object[] { id });
    }

    public User getUserByName(String name){
        return jdbcTemplate.queryForObject("SELECT id,name,password,createdAt FROM users WHERE name = ?", userRowMapper,new Object[] { name } );
    }

    public User signin(String name, String password) {
        logger.info("try login by {}...", name);
        User user = getUserByName(name);
        if (user.getPassword().equals(password)) {
            return user;
        }
        throw new RuntimeException("login failed.");
    }

    public User register(String name, String password) {
        logger.info("try register by {}...", name);
        User user = new User();
        user.setPassword(password);
        user.setName(name);
        user.setCreatedAt(System.currentTimeMillis());
        KeyHolder holder = new GeneratedKeyHolder();
        if (1 != jdbcTemplate.update((conn) -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO users(name,password,createdAt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, user.getName());
            ps.setObject(2, user.getPassword());
            ps.setObject(3, user.getCreatedAt());
            return ps;
        }, holder)) {
            throw new RuntimeException("Insert failed.");
        }
        user.setId(holder.getKey().longValue());
        return user;
    }
}
