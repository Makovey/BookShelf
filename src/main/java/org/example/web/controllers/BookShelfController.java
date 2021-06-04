package org.example.web.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.app.exceptions.BookNotFoundException;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.example.web.dto.BookFiltered;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.BookToDelete;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;


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
        model.addAttribute("bookToDelete", new BookToDelete());
        model.addAttribute("bookFiltered", new BookFiltered());
        model.addAttribute("bookList", bookService.getAllBooks());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookToDelete", new BookToDelete());
            model.addAttribute("bookFiltered", new BookFiltered());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        }
        if (!book.getAuthor().isEmpty() || !book.getTitle().isEmpty() || book.getSize() != null) {
            bookService.saveBook(book);
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookToDelete book, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("bookFiltered", new BookFiltered());
            return "book_shelf";
        }
        log.info("Delete books with parameter: " + book);
        bookService.removeAllByParameter(book);
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove/by-id")
    public String removeBookById(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookToDelete", new BookToDelete());
            model.addAttribute("bookList", bookService.getAllBooks());
            model.addAttribute("bookFiltered", new BookFiltered());
            return "book_shelf";
        } else {
            bookService.removeBookById(bookIdToRemove.getId());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/sort")
    public String sortBooks(@RequestParam(value = "sortBy") String sortBy, Model model) {
        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookToDelete", new BookToDelete());
        model.addAttribute("bookFiltered", new BookFiltered());
        model.addAttribute("bookList", bookService.sort(sortBy));
        return "book_shelf";
    }

    @PostMapping("/filter")
    public String filterBooks(@Valid BookFiltered book, BindingResult bindingResult, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("bookToDelete", new BookToDelete());
            model.addAttribute("bookList", bookService.getAllBooks());
            return "book_shelf";
        }

        if (book.getAuthor() != null && !book.getAuthor().isEmpty()) {
            log.info("Filter by author : " + book.getAuthor());
            model.addAttribute("bookList", bookService.filterByAuthor(book.getAuthor()));
        } else if (book.getTitle() != null && !book.getTitle().isEmpty()) {
            log.info("Filter by title : " + book.getTitle());
            model.addAttribute("bookList", bookService.filterByTitle(book.getTitle()));
        } else if (book.getSize() != null) {
            log.info("Filter by page : " + book.getSize());
            model.addAttribute("bookList", bookService.filterBySize(book.getSize()));
        } else {
            log.info("Return original list");
            model.addAttribute("bookList", bookService.getAllBooks());
        }

        model.addAttribute("book", new Book());
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("bookToDelete", new BookToDelete());
        return "book_shelf";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        if(file.getSize() == 0) {
            throw new BookNotFoundException("Please, choose book before upload");
        }

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

    @ExceptionHandler(BookNotFoundException.class)
    public String handleError(Model model, BookNotFoundException exception) {
        model.addAttribute("errorMessage", exception.getMessage());
        return "errors/file_not_found";
    }

    @PostMapping("/download")
    public String download(@RequestParam("fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String rootPath = System.getProperty("catalina.home");
        File dir = new File(rootPath + File.separator + "external_uploads");
        File serverFile = new File(dir.getAbsolutePath() + File.separator + fileName);

        FileInputStream in = new FileInputStream(serverFile);
        byte[] content = new byte[(int) serverFile.length()];
        in.read(content);
        ServletContext sc = request.getSession().getServletContext();
        String mimetype = sc.getMimeType(serverFile.getName());
        response.reset();
        response.setContentType(mimetype);
        response.setContentLength(content.length);
        response.setHeader("Content-Disposition", "attachment; filename=\"" + serverFile.getName() + "\"");
        FileCopyUtils.copy(content, response.getOutputStream());
        in.close();
        return "redirect:/books_shelf";
    }
}
