package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.services.UserService;
import org.example.web.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
@Slf4j
public class RegistrationController {

    private final UserService userService;

    @GetMapping
    public String registration(Model model) {
        log.info("GET registration page");
        model.addAttribute("user", new User());
        return "registration_page";
    }

    @PostMapping("/save")
    public String saveUser(User user) {
        log.info("POST registration page");
        userService.saveUser(user);
        return "redirect:/login";
    }
}