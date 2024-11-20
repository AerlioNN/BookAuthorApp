package com.example.BookAuthorApp;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void getAllAuthors_ShouldReturnListOfAuthors() throws Exception {
        Author author1 = new Author(1L, "Author 1");
        Author author2 = new Author(2L, "Author 2");
        List<Author> authors = Arrays.asList(author1, author2);

        when(authorService.getAllAuthors()).thenReturn(authors);

        mockMvc.perform(get("/authors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("Author 1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Author 2"));

        verify(authorService, times(1)).getAllAuthors();
    }

    @Test
    void getAuthorWithBooks_ShouldReturnAuthorWithBooks() throws Exception {
        Author author = new Author(1L, "Author 1");
        Book book1 = new Book(1L, "Book 1", 1L);
        Book book2 = new Book(2L, "Book 2", 1L);
        List<Book> books = Arrays.asList(book1, book2);
        AuthorWithBooks authorWithBooks = new AuthorWithBooks(author, books);

        when(authorService.getAuthorWithBooks(1L)).thenReturn(ResponseEntity.ok(authorWithBooks));

        mockMvc.perform(get("/authors/1/with-books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.author.id").value(1L))   // Accessing author details
                .andExpect(jsonPath("$.author.name").value("Author 1"))
                .andExpect(jsonPath("$.books[0].id").value(1L))  // Accessing book details
                .andExpect(jsonPath("$.books[0].title").value("Book 1"))
                .andExpect(jsonPath("$.books[1].id").value(2L))
                .andExpect(jsonPath("$.books[1].title").value("Book 2"));

        verify(authorService, times(1)).getAuthorWithBooks(1L);
    }

    @Test
    void addAuthor_ShouldCallAddAuthorService() throws Exception {
        Author author = new Author(1L, "New Author");

        doNothing().when(authorService).addAuthor(any(Author.class));

        mockMvc.perform(post("/authors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"New Author\"}"))
                .andExpect(status().isOk());

        verify(authorService, times(1)).addAuthor(any(Author.class));
    }

    @Test
    void updateAuthor_ShouldCallUpdateAuthorService() throws Exception {
        Author updatedAuthor = new Author(1L, "Updated Author");

        doNothing().when(authorService).updateAuthor(eq(1L), any(Author.class));

        mockMvc.perform(put("/authors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Updated Author\"}"))
                .andExpect(status().isOk());

        verify(authorService, times(1)).updateAuthor(eq(1L), any(Author.class));
    }

    @Test
    void deleteAuthor_ShouldCallDeleteAuthorService() throws Exception {
        doNothing().when(authorService).deleteAuthor(1L);

        mockMvc.perform(delete("/authors/1"))
                .andExpect(status().isOk());

        verify(authorService, times(1)).deleteAuthor(1L);
    }

    @Test
    void deleteAll_ShouldCallDeleteAllAuthorsService() throws Exception {
        doNothing().when(authorService).deleteAll();

        mockMvc.perform(delete("/authors"))
                .andExpect(status().isOk());

        verify(authorService, times(1)).deleteAll();
    }
}
