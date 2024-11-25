package com.example.BookAuthorApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AuthorService authorService;

    // GET - wszystkich autorów
    @GetMapping
    public List<Author> getAllAuthors() {
        return authorService.getAllAuthors();
    }

    // GET - autor z jego książkami
    @GetMapping("/{id}/with-books")
    public ResponseEntity<AuthorWithBooks> getAuthorWithBooks(@PathVariable Long id) {
        return authorService.getAuthorWithBooks(id);
    }

    // POST
    @PostMapping
    public void addAuthor(@RequestBody Author author) {
        authorService.addAuthor(author);
    }

    // PUT
    @PutMapping("/{id}")
    public void updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        authorService.updateAuthor(id,author);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public void deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
    }

    // DELETE - wszystkich autorów
    @DeleteMapping
    public void deleteAll(){
        authorService.deleteAll();
    }
}

