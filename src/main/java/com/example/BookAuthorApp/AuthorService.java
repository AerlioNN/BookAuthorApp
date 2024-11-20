package com.example.BookAuthorApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class AuthorService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // GET - wszystkich autorów
    public List<Author> getAllAuthors() {
        return jdbcTemplate.query(
                "SELECT id, name FROM authors",
                (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("name"))
        );
    }

    // GET - autor z jego książkami
    public ResponseEntity<AuthorWithBooks> getAuthorWithBooks(@PathVariable Long id) {
        Author author = jdbcTemplate.queryForObject(
                "SELECT id, name FROM authors WHERE id = ?",
                (rs, rowNum) -> new Author(rs.getLong("id"), rs.getString("name")), id);

        List<Book> books = jdbcTemplate.query(
                "SELECT id, title, author_id FROM books WHERE author_id = ?",
                (rs, rowNum) -> new Book(rs.getLong("id"), rs.getString("title"), rs.getLong("author_id")),
                id
        );

        return ResponseEntity.ok(new AuthorWithBooks(author, books));
    }

    // POST
    public void addAuthor(@RequestBody Author author) {
        jdbcTemplate.update("INSERT INTO authors (name) VALUES (?)", author.getName());
    }

    // PUT
    public void updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        jdbcTemplate.update("UPDATE authors SET name = ? WHERE id = ?", author.getName(), id);
    }

    // DELETE
    public void deleteAuthor(@PathVariable Long id) {
        jdbcTemplate.update("DELETE FROM authors WHERE id = ?", id);
    }

    // DELETE - wszystkich autorów
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM authors");
    }
}
