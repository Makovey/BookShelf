package org.example.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.User;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepository implements MyRepository<User> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

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
        mapSqlParameterSource.addValue("password", user.getPassword());
        jdbcTemplate.update("INSERT INTO users (username, password) VALUES (:username, :password)", mapSqlParameterSource);
    }

    @Override
    public boolean removeItemById(Long id) {
        throw new UnsupportedOperationException("This method is not for this class");
    }

    public Optional<User> findUserByUsername(String username) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM users where username = :username",
                new MapSqlParameterSource("username", username),
                (rs, rowNum) ->
                        Optional.of(new User(
                                rs.getLong("id"),
                                rs.getString("username"),
                                rs.getString("password")
                        ))
        );
    }

}
