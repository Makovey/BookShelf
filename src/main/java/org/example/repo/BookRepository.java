package org.example.repo;

import lombok.extern.slf4j.Slf4j;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class BookRepository implements MyRepository<Book> {

    private final List<Book> repo = new ArrayList<>();

    @Override
    public List<Book> getAll() {
        return repo;
    }

    @Override
    public void saveItem(Book book) {
        book.setId((long) book.hashCode());
        log.info("Saving book " + book);
        repo.add(book);
    }

    @Override
    public boolean removeItemById(Long id) {
        for (Book book : getAll()) {
            if (book.getId().equals(id)) {
                log.info("Deleting book " + book);
                return repo.remove(book);
            }
        }
        return false;
    }
}
