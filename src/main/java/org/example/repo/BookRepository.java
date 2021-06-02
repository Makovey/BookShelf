package org.example.repo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.Book;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class BookRepository implements MyRepository<Book> {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public List<Book> getAll() {
        return jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int row) -> {
            Book book = new Book();
            book.setId(rs.getLong("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getLong("size"));
            return book;
        });
    }

    @Override
    public void saveItem(Book book) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("author", book.getAuthor());
        mapSqlParameterSource.addValue("title", book.getTitle());
        mapSqlParameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books (author, title, size ) VALUES (:author, :title, :size)", mapSqlParameterSource);
        log.info("Saving book " + book);
    }

    @Override
    public boolean removeItemById(Long id) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
        mapSqlParameterSource.addValue("id", id);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", mapSqlParameterSource);
        log.info("remove book by id " + id + " completed");
        return true;
    }

    @Override
    public void removeByParameter(Book book) {
        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();

        if (!book.getAuthor().isEmpty()) {
            mapSqlParameterSource.addValue("author", book.getAuthor());
            jdbcTemplate.update("DELETE FROM books WHERE author = :author", mapSqlParameterSource);
        }
        if (!book.getTitle().isEmpty()) {
            mapSqlParameterSource.addValue("title", book.getTitle());
            jdbcTemplate.update("DELETE FROM books WHERE title = :title", mapSqlParameterSource);
        }
        if (book.getSize() != null) {
            mapSqlParameterSource.addValue("size", book.getSize());
            jdbcTemplate.update("DELETE FROM books WHERE size = :size", mapSqlParameterSource);
        }
    }
}
