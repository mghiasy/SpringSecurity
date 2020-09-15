package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class TemplateController {

    @GetMapping("login")
    public String getLoginView(){
        System.out.println("logiiiiiiin");
        return "loginPage";
    }

    @GetMapping("courses")
    public String getCourses(){
        System.out.println("courses");
        return "courses";
    }
}
