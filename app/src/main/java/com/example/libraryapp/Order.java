package com.example.libraryapp;

public class Order {
    private  String Id;
    private String orderId;
    private String customerName;
    private String customerId;
    private String email;
    private String bookName;
    private int qty;
    private String rate;
    private Double total;

    public Order() {
    }

    public Order(String id, String orderId, String customerName, String customerId, String email, String bookName, int qty, String rate, Double total) {
        Id = id;
        this.orderId = orderId;
        this.customerName = customerName;
        this.customerId = customerId;
        this.email = email;
        this.bookName = bookName;
        this.qty = qty;
        this.rate = rate;
        this.total = total;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
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
