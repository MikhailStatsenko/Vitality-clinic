package com.vitality.clinic.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
    @GetMapping("/home")
    public String homePage() {
        return "home";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
