package com.company;

import java.util.ArrayList;

public class Shop {
    ArrayList<Product> products = new ArrayList<>();

    void addProduct (Product product) {
        products.add(product);
    }

    void removeProduct (Product product) {
        products.remove(product);
    }

    Product getProduct(int index) {
        return products.get(index);
    }

    ArrayList<Product> getProducts() {
        return products;
    }

    int searchForAProductByNameInTheStore(String nameOfProduct) {
        for(int i = 0; i < products.size(); i++) {
            if (nameOfProduct.toLowerCase().equals(getProduct(i).getNameOfProduct().toLowerCase())){
                return i;
            }
        }
        return -1;
    }

}
