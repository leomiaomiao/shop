package com.lm.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class IndexController {

    private static final String INDEX = "index";

    //首页
    @RequestMapping("/")
    public String index(){
        System.out.println("sss");
        return INDEX;
    }
}

