package com.example.BookAuthorApp;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JdbcTemplate jdbcTemplate;

    @Test
    void getAllBooks_ShouldReturnListOfBooks() throws Exception {
        List<Book> books = Arrays.asList(
                new Book(1L, "Book 1", 1L),
                new Book(2L, "Book 2", 1L)
        );

        when(bookService.getAllBooks()).thenReturn(books);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[1].title").value("Book 2"));
    }

    @Test
    void getBookWithAuthor_ShouldReturnBookWithAuthor() throws Exception {
        BookWithAuthor bookWithAuthor = new BookWithAuthor(1L, "Book 1", 1L, "AuthorName");

        when(bookService.getBookWithAuthor(1L)).thenReturn(ResponseEntity.ok(bookWithAuthor));

        mockMvc.perform(get("/books/1/with-author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Book 1"))
                .andExpect(jsonPath("$.authorName").value("AuthorName"));
    }

    @Test
    void addBook_ShouldCallAddBookService() throws Exception {
        Book book = new Book(1L, "Book1", 1L);

        doNothing().when(bookService).addBook(Mockito.any(Book.class));

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"Book1\",\"authorId\":1}"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).addBook(Mockito.argThat(bookArg ->
                bookArg.getId().equals(1L) &&
                        bookArg.getTitle().equals("Book1") &&
                        bookArg.getAuthorId().equals(1L)
        ));
    }


    @Test
    void updateBook_ShouldCallUpdateBookService() throws Exception {
        Book updatedBook = new Book(1L, "UpdatedBook", 1L);

        mockMvc.perform(put("/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"title\":\"UpdatedBook\",\"authorId\":1}"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).updateBook(eq(1L), Mockito.argThat(bookArg ->
                bookArg.getId().equals(1L) &&
                        bookArg.getTitle().equals("UpdatedBook") &&
                        bookArg.getAuthorId().equals(1L)
        ));
    }


    @Test
    void deleteBook_ShouldCallDeleteBookService() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteBook(1L);
    }

    @Test
    void deleteAll_ShouldCallDeleteAllService() throws Exception {
        mockMvc.perform(delete("/books"))
                .andExpect(status().isOk());

        verify(bookService, times(1)).deleteAll();
    }
}
