package org.example.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public boolean authenticate(User user) {
        log.info("Trying to authenticate with user" + user);
        for(User savedUser :userService.getAllUsers()) {
            if(user.getPassword().equals(passwordEncoder.encode(savedUser.getPassword()))) return true;
        }

        return false;
    }
}
