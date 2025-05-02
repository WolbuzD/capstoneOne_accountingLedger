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
        do {
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
                    displayLedgerScreen();
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
                    System.out.println("Invalid Input! Try again.");
            }

        } while (!choice.equals("X"));
    }

    private static void showHomeScreen() {
        System.out.println("\n---- Home Screen ----");
        System.out.println("D) Add Deposit");
        System.out.println("P) Make a Payment (Debit)");
        System.out.println("L) Ledger Screen");
        System.out.println("E) Edit a Transaction");
        System.out.println("DEL) Delete a Transaction");
        System.out.println("S) Summary Report");
        System.out.println("X) Exit");
        System.out.print("Choose an option: ");
    }

    private static void addDeposit() {
        try {
            currentTransaction = createTransaction(true);
            saveTransaction();
            transactions.add(currentTransaction);
            System.out.println("Deposit added successfully!");
        } catch (Exception e) {
            System.out.println("Error adding deposit: " + e.getMessage());
        }
    }

    private static void makePayment() {
        try {
            currentTransaction = createTransaction(false);
            saveTransaction();
            transactions.add(currentTransaction);
            System.out.println("Payment made successfully!");
        } catch (Exception e) {
            System.out.println("Error making payment: " + e.getMessage());
        }
    }

    private static Transaction createTransaction(boolean isDeposit) {
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = now.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        System.out.println("Enter description: ");
        String description = scanner.nextLine();
        System.out.println("Enter vendor: ");
        String vendor = scanner.nextLine();
        double amount = 0;

        boolean validAmount = false;
        while (!validAmount) {
            System.out.println("Enter amount: ");
            try {
                amount = Double.parseDouble(scanner.nextLine());
                validAmount = true; // Amount parsed successfully, break the loop
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid amount.");
            }
        }

        if (!isDeposit) {
            amount = -Math.abs(amount);
        }

        return new Transaction(date, time, description, vendor, amount);
    }

    private static void saveTransaction() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            writer.write(currentTransaction.getDate() + "|" + currentTransaction.getTime() + "|" +
                    currentTransaction.getDescription() + "|" + currentTransaction.getVendor() + "|" +
                    currentTransaction.getAmount());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving transaction: " + e.getMessage());
        }
    }

    private static void saveAllTransactions() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Transaction t : transactions) {
                writer.write(t.getDate() + "|" + t.getTime() + "|" +
                        t.getDescription() + "|" + t.getVendor() + "|" +
                        t.getAmount());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving transactions: " + e.getMessage());
        }
    }

    private static void displayLedgerScreen() {
        String choice;
        do {
            System.out.println("\n--- Ledger Screen ---");
            System.out.println("A) Display all entries");
            System.out.println("D) Display only Deposits");
            System.out.println("P) Display only Payments (Debits)");
            System.out.println("R) Reports Screen");
            System.out.println("H) Go back Home");
            System.out.print("Choose an option: ");

            choice = scanner.nextLine().toUpperCase();
            switch (choice) {
                case "A":
                    displayAllTransactions();
                    break;
                case "D":
                    displayAllDeposits();
                    break;
                case "P":
                    displayAllPayments();
                    break;
                case "R":
                    displayReportScreen();
                    break;
                case "H":
                    System.out.println("Going back to Home Screen!");
                    break;
                default:
                    System.out.println("Invalid option. Try again!");
            }
        } while (!choice.equals("H"));
    }

    private static void displayAllTransactions() {
        boolean found = false;
        for (Transaction transaction : transactions) {
            System.out.println(transaction);
            found = true;
        }
        if (!found) {
            System.out.println("No transactions found.");
        }
    }

    private static void displayAllDeposits() {
        boolean found = false;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > 0) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No deposits found.");
        }
    }

    private static void displayAllPayments() {
        boolean found = false;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                System.out.println(transaction);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No payments found.");
        }
    }

    private static void displayReportScreen() {
        String choice;
        do {
            System.out.println("\n--- Report Menu ---");
            System.out.println("1) Month To Date");
            System.out.println("2) Previous Month");
            System.out.println("3) Year To Date");
            System.out.println("4) Previous Year");
            System.out.println("5) Search By Vendor");
            System.out.println("6) Custom Search"); // Custom search option added
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
                case "6":
                    customSearch(); // Custom search functionality
                    break;
                case "0":
                    System.out.println("Back to Report Page!");
                    break;
                default:
                    System.out.println("Invalid option. Try again!");
            }
        } while (!choice.equals("0"));
    }

    private static void reportMonthToDate() {
        try {
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
        } catch (Exception e) {
            System.out.println("Error generating Month-To-Date report: " + e.getMessage());
        }
    }

    private static void reportPreviousMonth() {
        try {
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
        } catch (Exception e) {
            System.out.println("Error generating Previous Month report: " + e.getMessage());
        }
    }

    private static void reportYearToDate() {
        try {
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
        } catch (Exception e) {
            System.out.println("Error generating Year-To-Date report: " + e.getMessage());
        }
    }

    private static void reportPreviousYear() {
        try {
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
        } catch (Exception e) {
            System.out.println("Error generating Previous Year report: " + e.getMessage());
        }
    }

    private static void searchByVendor() {
        try {
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
        } catch (Exception e) {
            System.out.println("Error searching by vendor: " + e.getMessage());
        }
    }

    // Custom search method
    private static void customSearch() {
        try {
            System.out.println("\n--- Custom Search ---");
            System.out.print("Enter start date (YYYY-MM-DD) or leave blank: ");
            String startDateInput = scanner.nextLine();
            System.out.print("Enter end date (YYYY-MM-DD) or leave blank: ");
            String endDateInput = scanner.nextLine();
            System.out.print("Enter description or leave blank: ");
            String descriptionInput = scanner.nextLine();
            System.out.print("Enter vendor name or leave blank: ");
            String vendorInput = scanner.nextLine();
            System.out.print("Enter amount (positive for deposit, negative for payment) or leave blank: ");
            String amountInput = scanner.nextLine();

            LocalDate startDate = startDateInput.isEmpty() ? null : LocalDate.parse(startDateInput);
            LocalDate endDate = endDateInput.isEmpty() ? null : LocalDate.parse(endDateInput);
            Double amount = amountInput.isEmpty() ? null : Double.parseDouble(amountInput);

            boolean found = false;

            for (Transaction t : transactions) {
                LocalDate txDate = LocalDate.parse(t.getDate());

                if (startDate != null && txDate.isBefore(startDate)) continue;
                if (endDate != null && txDate.isAfter(endDate)) continue;
                if (!descriptionInput.isEmpty() && !t.getDescription().toLowerCase().contains(descriptionInput.toLowerCase())) continue;
                if (!vendorInput.isEmpty() && !t.getVendor().toLowerCase().contains(vendorInput.toLowerCase())) continue;
                if (amount != null && Double.compare(t.getAmount(), amount) != 0) continue;

                System.out.println(t);
                found = true;
            }

            if (!found) {
                System.out.println("No transactions found matching the criteria.");
            }
        } catch (Exception e) {
            System.out.println("Error with custom search: " + e.getMessage());
        }
    }



    private static void deleteTransaction() {
        try {
            if (transactions.isEmpty()) {
                System.out.println("No transactions to delete.");
                return;
            }

            System.out.println("\n--- Delete Transaction ---");
            for (int i = 0; i < transactions.size(); i++) {
                System.out.println((i + 1) + ") " + transactions.get(i));
            }

            System.out.print("Enter the number of the transaction to delete: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice < 1 || choice > transactions.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            transactions.remove(choice - 1);
            saveAllTransactions();
            System.out.println("Transaction deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error deleting transaction: " + e.getMessage());
        }
    }

    private static void editTransaction() {
        try {
            if (transactions.isEmpty()) {
                System.out.println("No transactions to edit.");
                return;
            }

            System.out.println("\n--- Edit Transaction ---");
            for (int i = 0; i < transactions.size(); i++) {
                System.out.println((i + 1) + ") " + transactions.get(i));
            }

            System.out.print("Enter the number of the transaction to edit: ");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice < 1 || choice > transactions.size()) {
                System.out.println("Invalid choice.");
                return;
            }

            Transaction transaction = transactions.get(choice - 1);

            System.out.println("Enter new description (leave blank to keep current): ");
            String newDescription = scanner.nextLine();
            if (!newDescription.isEmpty()) {
                transaction.setDescription(newDescription);
            }

            System.out.println("Enter new vendor (leave blank to keep current): ");
            String newVendor = scanner.nextLine();
            if (!newVendor.isEmpty()) {
                transaction.setVendor(newVendor);
            }

            System.out.println("Enter new amount (leave blank to keep current): ");
            String newAmount = scanner.nextLine();
            if (!newAmount.isEmpty()) {
                transaction.setAmount(Double.parseDouble(newAmount));
            }

            saveAllTransactions();
            System.out.println("Transaction updated successfully.");
        } catch (Exception e) {
            System.out.println("Error editing transaction: " + e.getMessage());
        }
    }

    private static void summaryReport() {
        try {
            double totalDeposits = 0;
            double totalPayments = 0;
            double balance = 0;

            for (Transaction transaction : transactions) {
                if (transaction.getAmount() > 0) {
                    totalDeposits += transaction.getAmount();
                } else {
                    totalPayments += transaction.getAmount();
                }
                balance += transaction.getAmount();
            }

            System.out.println("\n--- Summary Report ---");
            System.out.printf("Total Deposits: $%.2f\n", totalDeposits);
            System.out.printf("Total Payments: $%.2f\n", Math.abs(totalPayments));
            System.out.printf("Current Balance: $%.2f\n", balance);
        } catch (Exception e) {
            System.out.println("Error generating summary report: " + e.getMessage());
        }
    }

    private static void loadTransactions() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|");

                if (parts.length == 5) {
                    Transaction transaction = new Transaction(
                            parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4])
                    );
                    transactions.add(transaction);
                }
            }
            reader.close();
        } catch (Exception e) {
            System.out.println("Error loading transactions from " + FILE_NAME + ": " + e.getMessage());
        }
    }
}
