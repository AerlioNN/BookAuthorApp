package com.example.BookAuthorApp;

public class BookWithAuthor {
    private Long bookId;
    private String title;
    private Long authorId;
    private String authorName;

    public BookWithAuthor(Long bookId, String title, Long authorId, String authorName) {
        this.bookId = bookId;
        this.title = title;
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}