package com.ps;

import java.util.ArrayList;
import java.util.Scanner;

public class LedgerApp {
    static Scanner scanner = new Scanner(System.in);
    static final String FILE_NAME = "transactions.csv";
    static Transaction currentTransaction;
    static ArrayList<Transaction> transactions = new ArrayList<>();

    public static void main(String[] args) {
     loadTransactions();

     String choice;

     do{
     showHomeScreen();
     choice = scanner.nextLine().toUpperCase();
     
         switch (choice) {
             case "D":
                 addDeposit();
                 break;
             case "P":
                 makePayment();
                 break;
             case "L":
                 showLedgerScreen();
                 break;
             case "X":
                 System.out.println("Exiting. Thank you!");
                 break;
             default:
                 System.out.println("Invalid option. Try again.");
         }


     }while(!choice.equalsIgnoreCase("X"));
     

    }

    private static void showHomeScreen() {
    }

    private static void addDeposit() {
    }

    private static void makePayment() {
    }

    private static void showLedgerScreen() {
    }

    private static void loadTransactions() {
    }


}
