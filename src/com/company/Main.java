package com.company;

/* Shop Project v1.0
*  */

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    Scanner scanner = new Scanner(System.in);
    static Shop shop = new Shop();
    static Cart cart = new Cart();
    boolean isMainMenu = true;
    boolean isLogin = false;
    boolean quit;



    public static void main(String[] args) {
        Main main = new Main();

        File temp = new File("productData.txt");
        boolean exist = temp.exists();
        if(!exist) { // if the .txt file does not exist, creates one
            saveChanges();
        }
        loadItems(); // load products list from .txt file

        main.runMenu();
    }

    public int onlyIntegerInput() {
        String input = scanner.next();
        int num;
        try{
            num = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            num = -1;
        }
        return num;
    }

    public double onlyDoubleInput() {
        String input = scanner.next();
        double num;
        try{
            num = Double.parseDouble(input);
        } catch (NumberFormatException e) {
            num = -1;
        }
        return num;
    }

    public void runMenu() {
        while(!quit) {
            if(isMainMenu) {
                printMainMenu();
                int mainMenuChoice = onlyIntegerInput();
                actionForMainMenu(mainMenuChoice);
            } else {
                if(!isLogin) {
                    printCustomerMenu();
                    int choice = onlyIntegerInput();
                    actionForCustomerMenu(choice);
                } else {
                    printEmployeeMenu();
                    int choiceForEmployee = onlyIntegerInput();
                    actionForEmployeeMenu(choiceForEmployee);
                }
            }
        }
    }


    private void printMainMenu() {
        System.out.println("\nWelcome in our store.");
        System.out.println("1 - Customer menu.");
        System.out.println("2 - Employee menu.");
        System.out.println("0 - Exit application.");
        System.out.print("Enter your choice: ");
    }

    private void printCustomerMenu() {
        System.out.println("\nWhat do you want to do: ");
        System.out.println("1 - Print list of products.");
        System.out.println("2 - Add product to cart.");
        System.out.println("3 - Remove product from cart.");
        System.out.println("4 - Print products in your cart.");
        System.out.println("5 - Pay.");
        System.out.println("0 - Back to main menu.");
        System.out.print("Enter your choice: ");
    }

    private void printEmployeeMenu() {
        System.out.println("\nWhat do you want to do: ");
        System.out.println("1 - Print list of products.");
        System.out.println("2 - Add product to the offer.");
        System.out.println("3 - Add more amount to existing product.");
        System.out.println("4 - Remove product from the offer.");
        System.out.println("0 - Logout.");
        System.out.print("Enter your choice: ");
    }

    private void actionForMainMenu(int mainMenuChoice) {
        switch (mainMenuChoice) {
            case 0:
                System.out.println("Application successfully closed.");
                System.exit(0);
                break;
            case 1:
                isMainMenu = false;
                break;
            case 2:
                isMainMenu = false;
                loginToEmployeeMode();
                break;
            default:
                System.out.println("Wrong choice. Try again!");
        }
    }

    private void actionForCustomerMenu(int customerChoice) {
        switch (customerChoice) {
            case 0:
                while(!cart.getProducts().isEmpty()){
                    removeAllProductFromTheCartToTheStore();
                }
                isMainMenu = true;
                break;
            case 1:
                printListOfProductsInTheOffer();
                break;
            case 2:
                addProductToCart();
                break;
            case 3:
                removeProductFromCart();
                break;
            case 4:
                printListOfProductsInTheCart();
                break;
            case 5:
                payForProductsInCart();
                break;
            default:
                System.out.println("Wrong choice. Try again!");
        }
    }

    private void actionForEmployeeMenu(int choiceForEmployee) {
        switch (choiceForEmployee) {
            case 0:
                isLogin = false;
                isMainMenu = true;
                break;
            case 1:
                printListOfProductsInTheOffer();
                break;
            case 2:
                addProductToTheOffer();
                break;
            case 3:
                addMoreAmount();
                break;
            case 4:
                removeProductFromTheOffer();
                break;
            default:
                System.out.println("Wrong choice. Try again!");
        }
    }

    private void addProductToTheOffer() {
        String nameOfProduct;
        double price;
        int amountToAddToTheStore;

        printListOfProductsInTheOffer();

        System.out.print("Enter name of product which you want to add to the offer: ");
        scanner.nextLine();
        nameOfProduct = scanner.nextLine();

        int foundProduct = shop.searchForAProductByNameInTheStore(nameOfProduct);

        if(foundProduct < 0) {
            System.out.print("Enter its price: ");
            price = onlyDoubleInput();
            if(price < 0) {
                System.out.println("Wrong Input.");
            } else {
                System.out.print("Enter the amount: ");
                amountToAddToTheStore = onlyIntegerInput();

                if(amountToAddToTheStore < 0) {
                    System.out.println("Wrong input.");
                } else {
                    Product productToAdd = new Product(nameOfProduct, price, amountToAddToTheStore);

                    shop.addProduct(productToAdd);
                    saveChanges();
                }
            }
        } else {
            System.out.println("This product is already in the store. Do you want to add more amount of it (Y/N)");
            char decision = scanner.next().charAt(0);
            if(decision == 'Y' || decision == 'y') {
                System.out.println("Enter the number.");
                amountToAddToTheStore = onlyIntegerInput();
                int currentAmountInTheShop = shop.getProduct(foundProduct).getAmount();

                if(amountToAddToTheStore < 0) {
                    System.out.println("Wrong input.");
                } else {
                    shop.getProduct(foundProduct).setAmount(amountToAddToTheStore + currentAmountInTheShop);
                }
                saveChanges();
            } else if (decision == 'N' || decision == 'n'){
                runMenu();
            } else {
                System.out.println("Wrong input.");
            }
        }
    }

    private void addProductToCart() {
        if(shop.getProducts().isEmpty()){
            System.out.println("\nThere is no product in the offer");
        } else {
            printListOfProductsInTheOffer();

            System.out.print("\nEnter the item number you want to add to your cart: ");
            int productNumberToAddToCart = onlyIntegerInput();

            if(productNumberToAddToCart < 0 || productNumberToAddToCart > shop.getProducts().size()) {
                System.out.println("Wrong input.");
            } else {
                int foundInTheCart = cart.searchForAProductByNameInTheCart
                        (shop.getProduct(productNumberToAddToCart - 1).getNameOfProduct());
                if(foundInTheCart < 0) {
                    System.out.print("Enter amount you want to add: ");
                    int amountToAddToCart = onlyIntegerInput();
                    int currentAmountInTheShop = shop.getProduct(productNumberToAddToCart - 1).getAmount();

                    if(amountToAddToCart > shop.getProduct(productNumberToAddToCart - 1).getAmount()){
                        System.out.println("There are not so many items in stock.");
                    } else {
                        if(amountToAddToCart < 0) {
                            System.out.println("Wrong input.");
                        } else {
                            Product productToAddToCart =
                                    new Product(shop.getProduct(productNumberToAddToCart - 1).getNameOfProduct(),
                                            shop.getProduct(productNumberToAddToCart - 1).getPrice(),
                                            amountToAddToCart);
                            cart.addProductToCart(productToAddToCart);
                            shop.getProduct(productNumberToAddToCart - 1).
                                    setAmount(currentAmountInTheShop - amountToAddToCart);
//                            saveChanges();
                        }
                    }
                } else {
                    System.out.println("This product is already in your cart. Do you want to add more amount of it (Y/N)");
                    char decision = scanner.next().charAt(0);

                    if(decision == 'Y' || decision == 'y') {
                        System.out.println("Enter amount you want to add: ");
                        int amountToAddToCart2 = onlyIntegerInput();
                        int currentAmountInTheShop2 = shop.getProduct(productNumberToAddToCart - 1).getAmount();

                        if(amountToAddToCart2 < 0) {
                            System.out.println("Wrong input.");

                            if(amountToAddToCart2 > shop.getProduct(productNumberToAddToCart - 1).getAmount()){
                                System.out.println("There are not so many items in stock.");
                            } else {
                                shop.getProduct(productNumberToAddToCart - 1).
                                        setAmount(currentAmountInTheShop2 - amountToAddToCart2);

                                int actualCartAmount = cart.getProduct(foundInTheCart).getAmount();
                                cart.getProduct(foundInTheCart).setAmount(actualCartAmount + amountToAddToCart2);
                            }
                        }
//                        saveChanges();
                    } else if (decision == 'N' || decision == 'n'){
                        runMenu();
                    } else {
                        System.out.println("Wrong input.");
                    }
                }
            }
        }
    }

    private void removeProductFromTheOffer() {
        printListOfProductsInTheOffer();
        System.out.print("\nEnter the number of items you want to delete: ");
        int itemNumberToRemove = onlyIntegerInput();

        if(itemNumberToRemove < 0 || itemNumberToRemove > shop.getProducts().size()) {
            System.out.println("Wrong choice.");
        } else {
            System.out.print("Do you want to remove (1) product or reduce (2) its amount? ");

            int decision = onlyIntegerInput();

            if(decision == 1) {
                shop.removeProduct(shop.getProduct(itemNumberToRemove - 1));
                saveChanges();
            } else if (decision == 2) {
                System.out.print("Enter amount of product you want to reduce: ");
                int amountToReduce = onlyIntegerInput();

                int currentAmount = shop.getProduct(itemNumberToRemove - 1).getAmount();

                if(amountToReduce > currentAmount) {
                    System.out.println("There are not so many items in stock.");
                } else if(amountToReduce == currentAmount) {
                    shop.removeProduct(shop.getProduct(itemNumberToRemove - 1));
                    saveChanges();
                } else {
                    int newAmount = currentAmount - amountToReduce;
                    shop.getProduct(itemNumberToRemove - 1).setAmount(newAmount);
                    saveChanges();
                }
            } else {
                System.out.println("Wrong input.");
            }
        }
    }

    private static void saveChanges() {
        try{
            FileOutputStream writeData = new FileOutputStream("productData.txt");
            ObjectOutputStream writeStream = new ObjectOutputStream(writeData);

            writeStream.writeObject(shop.products);
            writeStream.flush();
            writeStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadItems() {
        try{
            FileInputStream readData = new FileInputStream("productData.txt");
            ObjectInputStream readStream = new ObjectInputStream(readData);

            shop.products = (ArrayList<Product>) readStream.readObject();
            readStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printListOfProductsInTheOffer() {
        ArrayList<Product> products = shop.getProducts();
        if(products.isEmpty()){
            System.out.println("\nThere is no product in offer.");
        } else {
            System.out.println("\nProduct(s) in the store: ");
            for(int i = 0; i < products.size(); i++) {
                System.out.println((i+1) + ") " + products.get(i).productInfo());
            }
        }
    }

    private void removeProductFromCart() {
        if(cart.getProducts().isEmpty()) {
            System.out.println("\nYour cart is empty.");
        } else {
            printListOfProductsInTheCart();
            System.out.print("\nEnter the item number you want to remove from your cart: ");
            int itemNumberToRemoveFromCart = onlyIntegerInput();

            if(itemNumberToRemoveFromCart < 0 || itemNumberToRemoveFromCart > cart.getProducts().size()) {
                System.out.println("Wrong choice.");
            } else {
                System.out.print("Do you want to remove (1) product or reduce (2) its amount?: ");

                int decision = onlyIntegerInput();

                if (decision == 1) {
                    int searchProductInTheStore = shop.searchForAProductByNameInTheStore
                            (cart.getProduct(itemNumberToRemoveFromCart - 1).getNameOfProduct());

                    int amountOfProductToBeReturned = cart.getProduct(itemNumberToRemoveFromCart - 1).getAmount();

                    int currentAmountOfProductInTheStore = shop.getProduct(searchProductInTheStore).getAmount();

                    int updatedAmount = amountOfProductToBeReturned + currentAmountOfProductInTheStore;

                    shop.getProduct(searchProductInTheStore).setAmount(updatedAmount);

                    System.out.println(cart.getProduct(itemNumberToRemoveFromCart - 1).getNameOfProduct() +
                            " has been successfully deleted from your cart.");
                    cart.removeProductFromCart(cart.getProduct(itemNumberToRemoveFromCart - 1));
                    saveChanges();
                } else if (decision == 2) {
                    System.out.print("Enter the amount you want to remove: ");
                    int amountToReduce = onlyIntegerInput();

                    if(amountToReduce < 0) {
                        System.out.println("Wrong input.");
                    } else if(amountToReduce > cart.getProduct(itemNumberToRemoveFromCart - 1).getAmount()) {
                        System.out.println("There are not so many items in your cart.");
                    } else {
                        int searchProductInTheStore = shop.searchForAProductByNameInTheStore
                                (cart.getProduct(itemNumberToRemoveFromCart - 1).getNameOfProduct());

                        int currentAmountOfProductInTheStore = shop.getProduct(searchProductInTheStore).getAmount();

                        int currentAmountOfProductInTheCart = cart.getProduct
                                (itemNumberToRemoveFromCart - 1).getAmount();

                        int updatedAmountInTheStore = amountToReduce + currentAmountOfProductInTheStore;

                        int updatedAmountInTheCart = currentAmountOfProductInTheCart - amountToReduce;

                        shop.getProduct(searchProductInTheStore).setAmount(updatedAmountInTheStore);

                        cart.getProduct(itemNumberToRemoveFromCart - 1).setAmount(updatedAmountInTheCart);

                        System.out.println(amountToReduce + " pieces of " +
                                cart.getProduct(itemNumberToRemoveFromCart - 1).getNameOfProduct() +
                                " has been successfully deleted from your cart.");
                        saveChanges();
                    }
                } else {
                    System.out.println("Wrong input.");
                }
            }
        }
    }

    private void printListOfProductsInTheCart() {
        if(cart.getProducts().isEmpty()) {
            System.out.println("\nYour cart is empty.");
        } else {
            ArrayList<Product> products = cart.getProducts();
            System.out.println("\nProducts in the cart: ");

            for(int i = 0; i < products.size(); i++) {
                System.out.println((i+1) + ") " + products.get(i).productInfo());
            }

        }
    }

    private void payForProductsInCart() {
        if(cart.getProducts().isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            ArrayList<Product> products = cart.getProducts();
            double sumOfPrices = 0;

            for (Product product : products) {
                int productAmount = product.getAmount();
                double itemPrice = product.getPrice();
                sumOfPrices += (itemPrice*productAmount);
            }

            System.out.println("You have " + products.size() + " items in your cart" +
                    " "  + "and you need to pay " + sumOfPrices);
            System.out.println("Enter the money into machine: ");

            double moneyPutInTheStore = onlyDoubleInput();
            if(moneyPutInTheStore < 0) {
                System.out.println("It's not a value. Try again.");
                payForProductsInCart();
            } else {
                double rest = moneyPutInTheStore - sumOfPrices;

                if(sumOfPrices <= moneyPutInTheStore) {
                    System.out.println("Thanks for shopping, please come again soon.\n" +
                            "You have " + String.format("%.2f", rest) + " rest. Don't forget to take it.");
                    products.clear();
                    isMainMenu = true;
                }
                else {
                    System.out.println("You pay to little.");
                    System.out.println("Do you want to try to pay again? (Y/N)");

                    char decisionToPayAgain = scanner.next().charAt(0);
                    if(decisionToPayAgain == 'Y' || decisionToPayAgain == 'y') {
                        payForProductsInCart();
                    } else if(decisionToPayAgain == 'N' || decisionToPayAgain == 'n') {
                        System.out.println("Do you want to remove some product from cart? (Y/N)");
                        char decisionToRemoveProductFromCart = scanner.next().charAt(0);
                        if(decisionToRemoveProductFromCart == 'Y' || decisionToRemoveProductFromCart == 'y') {
                            printListOfProductsInTheCart();
                            removeProductFromCart();
                            payForProductsInCart();
                        } else if(decisionToRemoveProductFromCart == 'N' || decisionToRemoveProductFromCart == 'n') {
                            products.clear();
                            isMainMenu = true;
                        }
                    }
                }
            }
        }
    }

    private void loginToEmployeeMode() {
        String password;
        System.out.print("\nEnter password to login (123): ");
        password = scanner.next();

        if(!password.equals("123")) {
            System.out.println("Wrong Password");
            System.out.println("Try again? (Y/N)");
            char decisionToTryLoginAgain = scanner.next().charAt(0);
            if(decisionToTryLoginAgain == 'Y' || decisionToTryLoginAgain == 'y') {
                loginToEmployeeMode();
            } else if(decisionToTryLoginAgain == 'N' || decisionToTryLoginAgain == 'n') {
                isMainMenu = true;
            }
        } else {
            isLogin = true;
        }
    }

    private void addMoreAmount() {
        printListOfProductsInTheOffer();
        System.out.print("Select the number of the product to which you want to add more amount: ");
        int itemNumber = onlyIntegerInput();
        if(itemNumber < 0 || itemNumber > shop.getProducts().size()) {
            System.out.println("Wrong input.");
        } else {
            System.out.print("Enter amount you want to add: ");
            int amountToAdd = onlyIntegerInput();
            if(amountToAdd < 0){
                System.out.println("Wrong input.");
            } else {
                int currentAmount = shop.getProduct(itemNumber - 1).getAmount();
                int newAmount = amountToAdd + currentAmount;
                shop.getProduct(itemNumber - 1).setAmount(newAmount);
            }
        }
        saveChanges();
    }

    private void removeAllProductFromTheCartToTheStore() {
        for(int i = 0; i < cart.getProducts().size(); i++) {

                int amountOfProductToReturn = cart.getProduct(i).getAmount();

                int foundProduct = shop.searchForAProductByNameInTheStore(cart.getProduct(i).getNameOfProduct());

                int currentAmountInTheStore = shop.getProduct(foundProduct).getAmount();

                int updatedStoreAmount = amountOfProductToReturn + currentAmountInTheStore;

                shop.getProduct(foundProduct).setAmount(updatedStoreAmount);

                cart.removeProductFromCart(cart.getProduct(i));
        }
        saveChanges();
    }
}
