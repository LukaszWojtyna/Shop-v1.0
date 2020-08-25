package com.company;

import java.util.ArrayList;

public class Cart {
    ArrayList<Product> products = new ArrayList<>();

    void addProductToCart (Product product){
        products.add(product);
    }

    void removeProductFromCart (Product product) {
        products.remove(product);
    }

    Product getProduct(int index) {
        return products.get(index);
    }

    int searchForAProductByNameInTheCart(String nameOfProduct) {
        for(int i = 0; i < products.size(); i++) {
            if (nameOfProduct.toLowerCase().equals(getProduct(i).getNameOfProduct().toLowerCase())){
                return i;
            }
        }
        return -1;
    }

    ArrayList<Product> getProducts() {
        return products;
    }
}
