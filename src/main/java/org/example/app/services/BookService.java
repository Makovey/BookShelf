package org.example.app.services;

import lombok.RequiredArgsConstructor;
import org.example.repo.BookRepository;
import org.example.web.dto.Book;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.retrieveAll();
    }

    public void saveBook(Book book){
        bookRepository.store(book);
    }

    public boolean removeBookById(Long id) {
        return bookRepository.removeItemById(id);
    }

}
