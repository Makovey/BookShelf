package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookShelfController {

    private final BookService bookService;

    @GetMapping("/shelf")
    public String books(Model model) {
        log.info("Getting books");
        model.addAttribute("book", new Book());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("save")
    public String saveBook(Book book) {
        if (!book.getAuthor().isEmpty() || !book.getTitle().isEmpty() || book.getSize() != null) {
            bookService.saveBook(book);
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "id", defaultValue = "0") Long id) {
        if (bookService.removeBookById(id)) {
            log.info("Successful, deleted book with id: " + id);
        } else {
            log.info("Error, shelf didn't have book with id: " + id);
        }
        return "redirect:/books/shelf";
    }
}
