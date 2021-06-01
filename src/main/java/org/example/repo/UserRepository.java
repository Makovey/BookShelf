package org.example.repo;

import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class UserRepository implements MyRepository<User> {

    List<User> repo = new ArrayList<>();

    @Override
    public List<User> getAll() {
        return repo;
    }

    @Override
    public void saveItem(User user) {
        log.info("Save user: " + user);
        repo.add(user);
    }

    @Override
    public boolean removeItemById(Long id) {
        throw new UnsupportedOperationException("This method is not for this class");
    }

}
