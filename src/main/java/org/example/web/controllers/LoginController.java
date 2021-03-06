package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.app.exceptions.BookShelfLoginException;
import org.example.app.services.LoginService;
import org.example.web.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping(value = "/login")
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping
    public String login(Model model) {
        log.info("GET /login -> login_page.html");
        model.addAttribute("user", new User());
        return "login_page";
    }

    @PostMapping("/auth")
    @SneakyThrows
    public String authenticate(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.info("Validation error");
            return "login_page";
        }
        loginService.loadUserByUsername(user.getUsername());
        log.info("login successful");
        return "redirect:/books/shelf";
    }

    @ExceptionHandler(BookShelfLoginException.class)
    public String handleError(Model model, BookShelfLoginException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/404";
    }

}
