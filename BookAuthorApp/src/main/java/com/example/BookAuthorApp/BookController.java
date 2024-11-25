package com.example.BookAuthorApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private BookService bookService;

    // GET - wszystkich książek
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    // GET - książka z danymi autora
    @GetMapping("/{id}/with-author")
    public ResponseEntity<BookWithAuthor> getBookWithAuthor(@PathVariable Long id) {
        return bookService.getBookWithAuthor(id);
    }

    // POST
    @PostMapping
    public void addBook(@RequestBody Book book) {
        bookService.addBook(book);
    }

    // PUT
    @PutMapping("/{id}")
    public void updateBook(@PathVariable Long id, @RequestBody Book book) {
        bookService.updateBook(id,book);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
    }

    // DELETE - wszystkie książki
    @DeleteMapping
    public void deleteAll(){
        bookService.deleteAll();
    }
}
