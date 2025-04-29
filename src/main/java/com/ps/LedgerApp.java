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
             case "E":
                 editTransaction();
                 break;
             case "DEL":
                 deleteTransaction();
                 break;
             case "S":
                 summaryReport();
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

    private static void editTransaction() {
    }

    private static void deleteTransaction() {
    }
    private static void summaryReport() {
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
        String choice;
        do {
            System.out.println("\n--- Reports Menu ---");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search by Vendor");
            System.out.println("0) Back");
            System.out.print("Choose an option: ");
            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    reportMonthToDate();
                    break;
                case "2":
                    reportPreviousMonth();
                    break;
                case "3":
                    reportYearToDate();
                    break;
                case "4":
                    reportPreviousYear();
                    break;
                case "5":
                    searchByVendor();
                    break;
                case "0":
                    // Go back
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        } while (!choice.equals("0"));
    }

    private static void reportMonthToDate() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfMonth(1);
        boolean found = false;

        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate());
            if (!date.isBefore(start)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions for this month.");
        }
    }

    private static void reportPreviousMonth() {
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.minusMonths(1).withDayOfMonth(1);
        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        boolean found = false;

        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate());
            if (!date.isBefore(firstDay) && !date.isAfter(lastDay)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions for this period.");
        }
    }

    private static void reportYearToDate() {
        LocalDate today = LocalDate.now();
        LocalDate start = today.withDayOfYear(1);
        boolean found = false;

        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate());
            if (!date.isBefore(start)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions for this period.");
        }
    }

    private static void reportPreviousYear() {
        LocalDate today = LocalDate.now();
        LocalDate firstDay = today.minusYears(1).withDayOfYear(1);
        LocalDate lastDay = firstDay.withDayOfYear(firstDay.lengthOfYear());
        boolean found = false;

        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate());
            if (!date.isBefore(firstDay) && !date.isAfter(lastDay)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions for this period.");
        }
    }

    private static void searchByVendor() {
        System.out.print("Enter vendor name: ");
        String vendorInput = scanner.nextLine();
        boolean found = false;

        for (Transaction transaction : transactions) {
            if (transaction.getVendor().equalsIgnoreCase(vendorInput)) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No transactions found for this vendor.");
        }
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
