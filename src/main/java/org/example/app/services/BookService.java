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
    private final List<Book> unfilteredFullList;

    public List<Book> getAllBooks() {
        return bookRepository.getAll();
    }

    public void saveBook(Book book) {
        bookRepository.saveItem(book);
        unfilteredFullList.add(book);
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
        replaceMainList(sortedBooks);
    }

    public void filterByAuthor(@NonNull String filterBy) {
        returnOriginalList();
        List<Book> filteredList = bookRepository
                .getAll()
                .stream()
                .filter(x -> x.getAuthor().toLowerCase().startsWith(filterBy.toLowerCase()))
                .collect(Collectors.toList());

        replaceMainList(filteredList);
    }

    public void filterByTitle(@NonNull String filterBy) {
        returnOriginalList();
        List<Book> filteredList = bookRepository
                .getAll()
                .stream()
                .filter(x -> x.getTitle().toLowerCase().startsWith(filterBy.toLowerCase()))
                .collect(Collectors.toList());

        replaceMainList(filteredList);
    }

    public void filterBySize(@NonNull String filterBy) {
        returnOriginalList();
        List<Book> filteredList = bookRepository
                .getAll()
                .stream()
                .filter(x -> String.valueOf(x.getSize()).startsWith(filterBy))
                .collect(Collectors.toList());

        replaceMainList(filteredList);
    }

    private void replaceMainList(List<Book> anotherList) {
        getAllBooks().clear();
        getAllBooks().addAll(anotherList);
    }

    public void returnOriginalList() {
        getAllBooks().clear();
        getAllBooks().addAll(unfilteredFullList);
    }
}
