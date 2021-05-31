package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.services.LoginService;
import org.example.web.dto.User;
import org.example.web.exceptions.BookShelfLoginException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
    public String authenticate(User user) throws BookShelfLoginException {
        if (loginService.authenticate(user)) {
            log.info("login successful");
            return "redirect:/books/shelf";
        } else {
            log.info("login fail, try again");
            throw new BookShelfLoginException("Invalid user or password");
        }
    }

    @ExceptionHandler(BookShelfLoginException.class)
    public String handleError(Model model, BookShelfLoginException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/404";
    }

}
