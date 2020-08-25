package com.company;

import java.io.Serializable;

public class Product implements Serializable {
    private final String nameOfProduct;
    private final double price;
    private int amount;

    public Product(String nameOfProduct, double price, int quantity) {
        this.nameOfProduct = nameOfProduct;
        this.price = price;
        this.amount = quantity;
    }

    public String productInfo() {
        return  nameOfProduct +
                ", Price: " + price + " PLN" + ", x" + amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public String getNameOfProduct() {
        return nameOfProduct;
    }

    public int getAmount() {
        return amount;
    }
}
