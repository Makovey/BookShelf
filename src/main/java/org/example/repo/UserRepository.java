package org.example.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository implements MyRepository<User> {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users", (ResultSet rs, int row) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            return user;
        });
    }

    @Override
    public void saveItem(User user) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("username", user.getUsername());
        mapSqlParameterSource.addValue("password", passwordEncoder.encode(user.getPassword()));
        jdbcTemplate.update("INSERT INTO users (username, password) VALUES (:username, :password)", mapSqlParameterSource);
    }

    @Override
    public boolean removeItemById(Long id) {
        throw new UnsupportedOperationException("This method is not for this class");
    }

}
