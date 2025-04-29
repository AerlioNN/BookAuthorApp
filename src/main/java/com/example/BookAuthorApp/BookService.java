package com.example.BookAuthorApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

        try {
            BookWithAuthor book = jdbcTemplate.queryForObject(query, (rs, rowNum) ->
                            new BookWithAuthor(
                                    rs.getLong("book_id"),
                                    rs.getString("title"),
                                    rs.getLong("author_id"),
                                    rs.getString("name"))
                    , id);
            return ResponseEntity.ok(book);
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(404).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    // POST
    public void addBook(@RequestBody Book book) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM authors WHERE id = ?", Integer.class, book.getAuthorId());

        if (count == null || count == 0) {
            throw new IllegalArgumentException("Author ID does not exist.");
        }
        jdbcTemplate.update("INSERT INTO books (title, author_id) VALUES (?, ?)", book.getTitle(), book.getAuthorId());
    }

    // PUT
    public void updateBook(@PathVariable Long id, @RequestBody Book book) {
        // Check if the book with the provided ID exists
        Integer bookCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM books WHERE id = ?", Integer.class, id);

        if (bookCount == null || bookCount == 0) {
            throw new IllegalArgumentException("Book ID does not exist.");
        }

        // Check if the provided author ID exists
        Integer authorCount = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM authors WHERE id = ?", Integer.class, book.getAuthorId());

        if (authorCount == null || authorCount == 0) {
            throw new IllegalArgumentException("Author ID does not exist.");
        }

        // Update the book
        jdbcTemplate.update(
                "UPDATE books SET title = ?, author_id = ? WHERE id = ?",
                book.getTitle(), book.getAuthorId(), id);
    }


    // DELETE
    public void deleteBook(@PathVariable Long id) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM books WHERE id = ?", Integer.class, id);

        if (count == null || count == 0) {
            throw new IllegalArgumentException("Book ID does not exist.");
        }
        jdbcTemplate.update("DELETE FROM books WHERE id = ?", id);
    }

    // DELETE - wszystkie książki
    public void deleteAll(){
        jdbcTemplate.update("DELETE FROM books");
    }
}
