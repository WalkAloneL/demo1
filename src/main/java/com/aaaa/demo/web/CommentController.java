package com.aaaa.demo.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.aaaa.demo.entity.Comment;
import com.aaaa.demo.entity.User;
import com.aaaa.demo.service.CommentService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CommentController {
    public static final String KEY_USER = "__user__";
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CommentService commentService;

    @GetMapping("/comment")
    ModelAndView showComments(HttpSession session){
        User user = (User)session.getAttribute(KEY_USER);
        Map<String,Object> model = new HashMap<>();
        if(user != null){
            model.put("user",user);
            List<Comment> comments = commentService.getComments(100, 0);
            model.put("comments",comments);
        }else{
            return new ModelAndView("redirect:/signin");
        }
        return new ModelAndView("comment.html",model);
    }
    @PostMapping("/comment")
    public ModelAndView addComment(@RequestParam("text") String text, HttpSession session) {
        try {
            User user = (User)session.getAttribute(KEY_USER);
            if(user != null){
                Comment comment = commentService.addComment(user,text);
            }
        } catch (RuntimeException e) {
            logger.info("add comment error");
            e.printStackTrace();
            return new ModelAndView("comment.html",Map.of("error","添加失败"));
        }
        return new ModelAndView("redirect:/comment");
    }
}
