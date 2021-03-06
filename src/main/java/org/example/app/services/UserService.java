package org.example.app.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.repo.UserRepository;
import org.example.web.dto.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;

    public List<User> getAllUsers() {
        return userRepo.getAll();
    }

    public void saveUser(User user) {
        userRepo.saveItem(user);
    }

    public boolean isUserPresent(User user) {
        return getAllUsers().contains(user);
    }

    public Optional<User> findUserByUsername(String userName) {
        return userRepo.findUserByUsername(userName);
    }

}
