package com.example.libraryapp;
public class BookModel {
    private String bookId;
    private String bookName;
    private String genre;
    private String bookAuthor;
    private String bookPrice;
    private String image_url;

    public BookModel() {

    }

    public BookModel(String bookId, String bookName, String genre, String bookAuthor, String bookPrice, String image_url) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.genre = genre;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.image_url = image_url;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}

