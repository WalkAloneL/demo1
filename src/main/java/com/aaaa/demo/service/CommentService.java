package com.aaaa.demo.service;

import java.sql.Statement;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import com.aaaa.demo.entity.Comment;
import com.aaaa.demo.entity.User;

@Component
public class CommentService {

    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    JdbcTemplate jdbcTemplate;

    RowMapper<Comment> commentMapper = new BeanPropertyRowMapper<>(Comment.class); 

    public Comment getUserById(long id) {
        return jdbcTemplate.queryForObject("SELECT id,name,password,createdAt FROM users WHERE id = ?", commentMapper, new Object[] { id });
    }
    
    public List<Comment> getComments(int limit,int offset){
        return jdbcTemplate.query("SELECT id,name,text,createdAt FROM comments LIMIT ? OFFSET ?",commentMapper,limit,offset);
    }

    public Comment addComment(User user, String text){
        Comment comment = new Comment();
        comment.setName(user.getName());
        comment.setText(text);
        comment.setCreatedAt(System.currentTimeMillis());
        KeyHolder key = new GeneratedKeyHolder();
        if(1 != jdbcTemplate.update((conn) -> {
            var ps = conn.prepareStatement("INSERT INTO comments(name,text,createdAt) VALUES(?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setObject(1, comment.getName());
            ps.setObject(2, comment.getText());
            ps.setObject(3, comment.getCreatedAt());
            return ps;
        }, key)){
            throw new RuntimeException("Comment insert failed.");
        }
        comment.setId(key.getKey().longValue());
        return comment;
    }

}
