package com.aaaa.demo.web;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.aaaa.demo.entity.User;
import com.aaaa.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {
    
    public static final String KEY_USER = "__user__";
    final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    UserService userService;

    @GetMapping("/")
    public ModelAndView index(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        Map<String, Object> model = new HashMap<>();
        if (user != null) {
            model.put("user", model);
        }
        return new ModelAndView("index.html", model);
    }

    @GetMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("register.html");
    }

    @PostMapping("/register")
    public ModelAndView doRegister(@RequestParam("name") String name, @RequestParam("password") String password) {
        try {
            User user = userService.register(name, password);
            logger.info("user registered: {}", user.getName());
        } catch (RuntimeException e) {
            return new ModelAndView("register.html", Map.of("name", name, "error", "注册失败"));
        }
        return new ModelAndView("redirect:/signin");
    }

    @GetMapping("/signin")
    public ModelAndView signin(HttpSession session) {
        User user = (User) session.getAttribute(KEY_USER);
        if (user != null) {
            return new ModelAndView("redirect:/comment");
        }
        return new ModelAndView("signin.html");
    }

    @PostMapping("/signin")
    public ModelAndView doSignin(@RequestParam("name") String name, @RequestParam("password") String password, HttpSession session) {
        try {
            User user = userService.signin(name, password);
            session.setAttribute(KEY_USER, user);
        } catch (RuntimeException e) {
            return new ModelAndView("signin.html", Map.of("name", name, "error", "登录失败"));
        }
        return new ModelAndView("redirect:/comment");
    }

    @GetMapping("/signout")
    public String signout(HttpSession session) {
        session.removeAttribute(KEY_USER);
        return "redirect:/signin";
    }
}
