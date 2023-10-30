package com.example.libraryapp;

public class CartModel {
    private String id;

    private String book_id;
    private String name;

    private int qty;
    private String rate;
    private Double total;

    public CartModel() {
    }

    public CartModel(String id, String book_id, String name, int qty, String rate, Double total) {
        this.id = id;
        this.book_id = book_id;
        this.name = name;
        this.qty = qty;
        this.rate = rate;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }
}
