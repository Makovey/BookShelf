package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;


@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
@Slf4j
public class BookShelfController {

    private final BookService bookService;

    @GetMapping("/shelf")
    public String getBooks(Model model) {
        log.info("Getting books");
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        }
        if (!book.getAuthor().isEmpty() || !book.getTitle().isEmpty() || book.getSize() != null) {
            bookService.saveBook(book);
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(Book book) {
        log.info("Trying to delete books with parameter: " + book);
        bookService.removeAllByParameter(book);
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove/by-id")
    public String removeBookById(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/sort")
    public String sortBooks(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "size", required = false) String size) {
        if (author != null) {
            log.info("Sort by author : " + author);
            bookService.sort(author);
        } else if (title != null) {
            log.info("Sort by title : " + title);
            bookService.sort(title);
        } else {
            log.info("Sort by page : " + size);
            bookService.sort(size);
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/filter")
    public String filterBooks(
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "size", required = false) String size) {
        if (!author.isEmpty()) {
            log.info("Filter by author : " + author);
            bookService.filterByAuthor(author);
        } else if (!title.isEmpty()) {
            log.info("Filter by title : " + title);
            bookService.filterByTitle(title);
        } else if (size != null) {
            log.info("Filter by page : " + size);
            bookService.filterBySize(size);
        } else {
            log.info("Return original list");
            bookService.returnOriginalList();
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        String fileName = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
        stream.write(bytes);
        stream.close();

        log.info("New file saved at: " + serverFile.getAbsolutePath());

        return "redirect:/books/shelf";
    }
}
