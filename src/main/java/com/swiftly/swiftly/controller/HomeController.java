package com.swiftly.swiftly.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Witaj w aplikacji Swiftly! Sprawdź dokumentację API pod /swagger-ui.html lub podobnym endpointem.";
    }
}
