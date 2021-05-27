package org.example.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {

    private final UserService userService;

    public boolean authenticate(User user) {
        log.info("Trying to authenticate with user" + user);
        if (user.getUsername().equals("admin") && user.getPassword().equals("admin")) return true;
        for (User savedUser : userService.getAllUsers()) {
            if (user.getUsername().equals(savedUser.getUsername())
                    && user.getPassword().equals(savedUser.getPassword())) return true;
        }
        return false;
    }
}
