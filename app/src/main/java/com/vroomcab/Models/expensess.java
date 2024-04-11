package com.vroomcab.Models;

public class expensess {

    String expense;
    String userid;
    String category;
    String amount;
    String productname;
    String date;

    public expensess(String expense, String amount, String category, String date, String productname) {
        this.expense = expense;
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.productname = productname;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
