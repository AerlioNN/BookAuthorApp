package com.example.BookAuthorApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Service
public class BookService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // GET - wszystkich książek
    public List<Book> getAllBooks() {
        return jdbcTemplate.query(
                "SELECT id, title, author_id FROM books",
                (rs, rowNum) -> new Book(rs.getLong("id"), rs.getString("title"), rs.getLong("author_id"))
        );
    }

    // GET - książka z danymi autora
    public ResponseEntity<BookWithAuthor> getBookWithAuthor(@PathVariable Long id) {
        String query = """
                SELECT b.id AS book_id, b.title, a.id AS author_id, a.name 
                FROM books b 
                JOIN authors a ON b.author_id = a.id 
                WHERE b.id = ?""";
        return jdbcTemplate.queryForObject(query, (rs, rowNum) ->
                ResponseEntity.ok(new BookWithAuthor(
                        rs.getLong("book_id"),
                        rs.getString("title"),
                        rs.getLong("author_id"),
                        rs.getString("name"))
                ), id);
    }

    // POST
    public void addBook(@RequestBody Book book) {
        jdbcTemplate.update("INSERT INTO books (title, author_id) VALUES (?, ?)", book.getTitle(), book.getAuthorId());
    }

    // PUT
    public void updateBook(@PathVariable Long id, @RequestBody Book book) {
        jdbcTemplate.update("UPDATE books SET title = ?, author_id = ? WHERE id = ?", book.getTitle(), book.getAuthorId(), id);
    }

    // DELETE
    public void deleteBook(@PathVariable Long id) {
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }

    // DELETE - wszystkie książki
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM books");
    }
}
