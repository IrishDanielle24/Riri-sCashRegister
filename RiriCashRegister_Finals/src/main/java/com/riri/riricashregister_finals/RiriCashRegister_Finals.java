/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.riri.riricashregister_finals;

/**
 *
 * @author Irish Danielle
 */
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RiriCashRegister_Finals {
    static Scanner riri = new Scanner(System.in);

    static ArrayList<String> usernames = new ArrayList<>();
    static ArrayList<String> passwords = new ArrayList<>();
    
    static String loggedInUser = "";

    // ====================== SIGNUP =========================
   static void signup() {
    System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~ SIGNUP ~~~~~~~~~~~~~~~~~~~~~~~~~~");
    while (true) {
        System.out.print("Enter username (5-15 alphanumeric characters): ");
        String username = riri.nextLine();

        Pattern usernamePattern = Pattern.compile("^[a-zA-Z0-9]{5,15}$");
        Matcher usernameMatcher = usernamePattern.matcher(username);
        if (!usernameMatcher.matches()) {
            System.out.println("Invalid username format. Please try again.");
            continue;
        }

        System.out.print("Enter password (8-20 chars, at least 1 uppercase and 1 number): ");
        String password = riri.nextLine();

        Pattern passwordPattern = Pattern.compile("^(?=.*[A-Z])(?=.*\\d).{8,20}$");
        Matcher passwordMatcher = passwordPattern.matcher(password);
        if (!passwordMatcher.matches()) {
            System.out.println("\nInvalid password format. Please try again.");
            continue;
        }

        usernames.add(username);
        passwords.add(password);
        System.out.println("Signup successful!\n");
        break;
    }
}

    // ======================= LOGIN ==========================
    static void login() {
        System.out.println("\n~~~~~~~~~~~~~~~~~~~~~~~~~~~ LOGIN ~~~~~~~~~~~~~~~~~~~~~~~~~~");
        while (true) {
            System.out.print("Enter username: ");
            String username = riri.nextLine();
            System.out.print("Enter password: ");
            String password = riri.nextLine();

            boolean found = false;
            for (int i = 0; i < usernames.size(); i++) {
                if (usernames.get(i).equals(username) && passwords.get(i).equals(password)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                loggedInUser = username;
                System.out.println("Login successful! Welcome " + username + "!\n");
                break;
            } else {
                System.out.println("Incorrect username or password. Try again.\n");
            }
        }
    }

    // =================== YOUR ORIGINAL METHODS BELOW ====================
    static void displayCart(ArrayList<String> orderNames, ArrayList<Integer> orderPrices, ArrayList<Integer> orderQuantities) {
        if (orderNames.isEmpty()) {
            System.out.println("Your cart is empty.");
        } else {
            int totalAmount = 0;
            System.out.println("```````````````````````````````````````````````````````````````");
            System.out.println("\t\t\tMY CART:");
            for (int i = 0; i < orderNames.size(); i++) {
                int itemTotal = orderPrices.get(i) * orderQuantities.get(i);
                totalAmount += itemTotal;
                System.out.println((i + 1) + ". " + orderNames.get(i) + " x" + orderQuantities.get(i) + " || @ PHP " + itemTotal);
            }
            System.out.println("\t\t\t\t\tTotal: PHP " + totalAmount);
        }
    }

    static void addItem(ArrayList<String> orderNames, ArrayList<Integer> orderPrices, ArrayList<Integer> orderQuantities, String[] menuItems, int[] prices) {
        int user, quantity;
        char active = 'y';
        while (active == 'y' || active == 'Y') {
            System.out.println("```````````````````````````````````````````````````````````````");
            System.out.println();
            System.out.print("Enter the number of your order: ");
            user = riri.nextInt() - 1;
            System.out.println();

            if (user < 0 || user >= menuItems.length) {
                System.out.println("Invalid choice. Please try again.");
            } else {
                System.out.print("Quantity: ");
                quantity = riri.nextInt();

                if (quantity > 0) {
                    orderNames.add(menuItems[user]);
                    orderPrices.add(prices[user]);
                    orderQuantities.add(quantity);
                } else {
                    System.out.println("Quantity must be at least 1.");
                }
            }
            
            displayCart(orderNames, orderPrices, orderQuantities);

            System.out.println();
            System.out.print("Do you want to add another? (y/n): ");
            riri.nextLine();
            active = riri.nextLine().charAt(0);
        }
    }

    static void removeItem(ArrayList<String> orderNames, ArrayList<Integer> orderPrices, ArrayList<Integer> orderQuantities) {
        if (orderNames.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to remove.");
            return;
        }

        displayCart(orderNames, orderPrices, orderQuantities);
        System.out.print("Select a number to remove: ");
        int choice = riri.nextInt();

        if (choice >= 1 && choice <= orderNames.size()) {
            orderNames.remove(choice - 1);
            orderPrices.remove(choice - 1);
            orderQuantities.remove(choice - 1);
            System.out.println("Item Removed.");
        } else {
            System.out.println("Invalid selection.");
        }
        displayCart(orderNames, orderPrices, orderQuantities);
    }

    static boolean checkoutCart(ArrayList<String> orderNames, ArrayList<Integer> orderPrices, ArrayList<Integer> orderQuantities) {
        if (orderNames.isEmpty()) {
            System.out.println("Your cart is empty. Nothing to checkout.");
            return true;
        }

        displayCart(orderNames, orderPrices, orderQuantities);

        int totalAmount = 0;
        for (int i = 0; i < orderNames.size(); i++) {
            totalAmount += (orderPrices.get(i) * orderQuantities.get(i));
        }

        System.out.print("Enter payment amount: PHP ");
        int payment = riri.nextInt();
        while (payment < totalAmount) {
            System.out.println("Insufficient payment. Please enter a valid amount.");
            System.out.print("Enter payment amount: PHP ");
            payment = riri.nextInt();
        }

        int change = payment - totalAmount;
        
        logTransaction(usernames.get(usernames.size() - 1), orderNames, orderPrices, orderQuantities, payment, change);
        
        System.out.println("```````````````````````````````````````````````````````````````");
        System.out.println();
        System.out.println("Payment received. Your change is: PHP " + change);
        System.out.println();
        System.out.println("------------Thank you for ordering from Rodillas!--------------");

        orderNames.clear();
        orderPrices.clear();
        orderQuantities.clear();

        System.out.println();
        System.out.println("```````````````````````````````````````````````````````````````");
        System.out.print("Do you want to perform another transaction? (y/n): ");
        char anotherTransaction = riri.next().charAt(0);
        System.out.println();
        return anotherTransaction == 'y' || anotherTransaction == 'Y';
    }
    
   public static void logTransaction(String username, ArrayList<String> items, ArrayList<Integer> prices, ArrayList<Integer> quantities, int payment, int change) {
    try (PrintWriter writer = new PrintWriter(new FileWriter("transaction_log.txt", true))) {
        // Get current date and time separately
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        String date = now.format(dateFormatter);
        String time = now.format(timeFormatter);

        writer.println("==========================================================");
        writer.println("                   RODILLAS TRANSACTION RECEIPT");
        writer.println("==========================================================");
        writer.println("Date      : " + date);
        writer.println("Time      : " + time);
        writer.println("Customer  : " + username);
        writer.println("----------------------------------------------------------");
        writer.println("Items:");
        
        int total = 0;
        for (int i = 0; i < items.size(); i++) {
            int itemTotal = prices.get(i) * quantities.get(i);
            total += itemTotal;
            writer.printf("%d. %-22s x%-2d @ PHP %-4d = PHP %d%n",
                    i + 1, items.get(i), quantities.get(i), prices.get(i), itemTotal);
        }

        writer.println("----------------------------------------------------------");
        writer.printf("Total     : PHP %d%n", total);
        writer.printf("Payment   : PHP %d%n", payment);
        writer.printf("Change    : PHP %d%n", change);
        writer.println("==========================================================");
        writer.println();
    } catch (IOException e) {
        System.out.println("An error occurred while writing to the transaction log.");
    }
}

    // ===================== MAIN STARTS =========================
    public static void main(String[] args) {
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("[                 Welcome to Rodillas Cash Register!         ]");
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        
        signup();
        login();

        boolean anotherTransaction = true;

        while (anotherTransaction) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("[                    Welcome to Rodillas!                    ]");
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            System.out.println("                            MENU                              ");
             System.out.println("                                                             ");
            System.out.println("                        Whole Cakes:                          ");
            System.out.println("1. Whole Yema Cake                                || @ PHP 600");
            System.out.println("2. Whole Caramel Cake                             || @ PHP 600");
            System.out.println("3. Whole 2 in 1 Yema Ube Cake                     || @ PHP 700");
            System.out.println("4. Whole 2 in 1 Yema Caramel                      || @ PHP 700");
            System.out.println("5. Whole 3 in 1 Yema, Caramel, Ube                || @ PHP 800");
            System.out.println("6. Whole Chocolate Cake                           || @ PHP 500");
            System.out.println("7. Whole Yema de Fruta                            || @ PHP 620");

            String[] menuItems = {
                "Whole Yema Cake", "Whole Caramel Cake",
                "Whole 2 in 1 Yema Ube Cake", "Whole 2 in 1 Yema Caramel",
                "Whole 3 in 1 Yema, Caramel, Ube Cake", "Whole Chocolate Cake",
                "Whole Yema de Fruta"
            };
            int[] prices = {600, 600, 700, 700, 800, 500, 620};

            ArrayList<String> orderNames = new ArrayList<>();
            ArrayList<Integer> orderPrices = new ArrayList<>();
            ArrayList<Integer> orderQuantities = new ArrayList<>();

            addItem(orderNames, orderPrices, orderQuantities, menuItems, prices);

            boolean active = true;
            while (active) {
                System.out.println("```````````````````````````````````````````````````````````````");
                System.out.println("What would you want to do next?");
                System.out.println("1. Add more items");
                System.out.println("2. Remove an item");
                System.out.println("3. Checkout my cart");
                System.out.println("4. Exit");
                System.out.println("```````````````````````````````````````````````````````````````");
                System.out.print("Enter a number: ");
                int choice = riri.nextInt();

                switch (choice) {
                    case 1:
                        addItem(orderNames, orderPrices, orderQuantities, menuItems, prices);
                        break;
                    case 2:
                        removeItem(orderNames, orderPrices, orderQuantities);
                        break;
                    case 3:
                        active = false;
                        anotherTransaction = checkoutCart(orderNames, orderPrices, orderQuantities);
                        break;
                    case 4:
                        active = false;
                        anotherTransaction = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        }
        System.out.println();
        System.out.println("```````````````````````````````````````````````````````````````");
        System.out.println("`                                                             `");
        System.out.println("` Thank you for shopping at Rodillas! Please come again. <333 `");
        System.out.println("`                                                             `");
        System.out.println("```````````````````````````````````````````````````````````````");
    }
}
