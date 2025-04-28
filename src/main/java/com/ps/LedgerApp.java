package com.ps;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        System.out.println("\n---Home Screen---");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make Payment(Debit)");
        System.out.println("L) Ledger");
        System.out.println("X) Exit");
        System.out.print("Choose an option: ");
    }

    private static void addDeposit() {
        currentTransaction = createTransaction(true);
        saveTransaction();
        transactions.add(currentTransaction);
        System.out.println("Deposit added successfully!");
    }

    private static void makePayment() {
        currentTransaction = createTransaction(false);
        saveTransaction();
        transactions.add(currentTransaction);
        System.out.println("Deposit added successfully!");
    }



    private static Transaction createTransaction(boolean isDeposit) {
        //I want to pass in a boolean that checks if it's a Deposit/debit
    //--- we need the date and time of the transaction, and prompt the user to add all the other infos
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.print("Enter a description: ");
        String description = scanner.nextLine();

        System.out.print("Enter a vendor: ");
        String vendor = scanner.nextLine();

        System.out.print("Enter an amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        if(!isDeposit){
            amount = -Math.abs(amount);
        }

        return new Transaction( date, time, description, vendor, amount);
    }


    private static void saveTransaction() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            writer.write(
                    currentTransaction.getDate() + "|" + currentTransaction.getTime() + "|" +
                            currentTransaction.getDescription() + "|" + currentTransaction.getVendor() + "|" +
                               currentTransaction.getAmount()
            );
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Error Saving transaction: " + e.getMessage());
        }
    }

    private static void showLedgerScreen() {
        String choice;
        do{
            System.out.println("\n---Ledger Screen---");
            System.out.println("A) Display all transactions");
            System.out.println("D) Display Deposits only");
            System.out.println("P) Display Payments(Debit) only");
            System.out.println("R) Go to Reports Menu");
            System.out.println("H) Go back Ledger Home");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine().toUpperCase();

            switch (choice){
                case "A":
                    displayAllTransactions();
                    break;
                case "D":
                    displayDeposits();
                    break;
                case "P":
                    displayPayments();
                    break;
                case "R":
                    showReportsScreen();
                    break;
                case "H":
                    // Return to home
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }

        }while(!choice.equalsIgnoreCase("H"));
    }

    private static void displayAllTransactions() {
       for(Transaction transaction: transactions){
           System.out.println(transaction);
       }
    }

    private static void displayDeposits() {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void displayPayments() {
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
            }
        }
    }

    private static void showReportsScreen() {
    }

    private static void loadTransactions() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
             String line;
             while((line = reader.readLine()) != null){
                 String[] parts = line.split("\\|");
                 if(parts.length == 5){
                     Transaction transaction = new Transaction(
                             parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4])
                     );

                     transactions.add(transaction);
                 }
             }
             reader.close();
        } catch (IOException e) {
            System.out.println("Error Loading transactions: " + e.getMessage());
        }
    }


}
