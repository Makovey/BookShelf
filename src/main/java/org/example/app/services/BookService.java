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
        bookRepository.removeByParameter(book);
    }

    public List<Book> sort(String sortItem) {
        List<Book> sortedBooks;
        if (sortItem.equals("author")) {
            sortedBooks = getAllBooks()
                    .stream()
                    .sorted(Comparator.comparing(Book::getAuthor, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        } else if (sortItem.equals("title")) {
            sortedBooks = getAllBooks()
                    .stream()
                    .sorted(Comparator.comparing(Book::getTitle, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        } else {
            sortedBooks = getAllBooks()
                    .stream()
                    .sorted(Comparator.comparing(Book::getSize, Comparator.nullsFirst(Comparator.naturalOrder())))
                    .collect(Collectors.toList());
        }
        return sortedBooks;
    }

    public List<Book> filterByAuthor(@NonNull String filterBy) {
        return bookRepository
                .getAll()
                .stream()
                .filter(x -> x.getAuthor().toLowerCase().startsWith(filterBy.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> filterByTitle(@NonNull String filterBy) {
        return bookRepository
                .getAll()
                .stream()
                .filter(x -> x.getTitle().toLowerCase().startsWith(filterBy.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Book> filterBySize(@NonNull String filterBy) {
        return bookRepository
                .getAll()
                .stream()
                .filter(x -> String.valueOf(x.getSize()).startsWith(filterBy))
                .collect(Collectors.toList());
    }
}
