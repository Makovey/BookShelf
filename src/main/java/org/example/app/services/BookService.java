package org.example.app.services;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.repo.BookRepository;
import org.example.web.dto.Book;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    public void saveBook(Book book) {
        bookRepository.saveItem(book);
    }

    public boolean removeBookById(Long id) {
        return bookRepository.removeItemById(id);
    }

    public void removeAllByParameter(Book book) {
        if (!book.getAuthor().isEmpty()) getAllBooks().removeIf(x -> x.getAuthor().equals(book.getAuthor()));
        if (!book.getTitle().isEmpty()) getAllBooks().removeIf(x -> x.getTitle().equals(book.getTitle()));
        if (book.getSize() != null)
            getAllBooks().removeIf(x -> x.getSize() != null && x.getSize().equals(book.getSize()));
    }

    public void sort(@NonNull String sortItem) {
        List<Book> sortedBooks;
        if (sortItem.equals("author")) {
            sortedBooks = getAllBooks()
                    .stream()
                    .sorted(Comparator.comparing(Book::getAuthor))
                    .collect(Collectors.toList());
        } else if (sortItem.equals("title")) {
            sortedBooks = getAllBooks()
                    .stream()
                    .sorted(Comparator.comparing(Book::getTitle))
                    .collect(Collectors.toList());
        } else {
            sortedBooks = getAllBooks()
                    .stream()
                    .sorted(Comparator.comparingLong(Book::getSize))
                    .collect(Collectors.toList());
        }
        getAllBooks().clear();
        getAllBooks().addAll(sortedBooks);
    }
}
