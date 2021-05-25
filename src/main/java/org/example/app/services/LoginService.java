package org.example.app.services;

import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.LoginForm;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LoginService {
    public boolean authenticate(LoginForm loginForm){
        log.info("Trying to authenticate with login-form" + loginForm);
        return (loginForm.getUsername().equals("admin") && loginForm.getPassword().equals("admin"));
    }
}
