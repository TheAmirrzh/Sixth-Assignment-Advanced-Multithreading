package Banking;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionProcessor implements Runnable {
    private final Map<String, BankAccount> accounts;
    private String fileName;

    public TransactionProcessor(Map<String, BankAccount> accounts) {
        this.accounts = accounts;
    }

    // This constructor is needed for backward compatibility with BankingMain
    public TransactionProcessor(String fileName, List<BankAccount> accountsList) {
        this.fileName = fileName;
        this.accounts = new HashMap<>();
        for (BankAccount account : accountsList) {
            accounts.put(account.getAccountId(), account);
        }
    }

    @Override
    public void run() {
        if (fileName != null) {
            processTransactions(fileName);
        }
    }

    public void processTransactions(String filePath) {
        File file = new File(filePath);

        // Print current path info for debugging
        System.out.println("Looking for file: " + file.getAbsolutePath());

        if (!file.exists()) {
            System.err.println("Transaction file not found: " + filePath);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length < 2) continue;

                String action = parts[0].trim();

                switch (action) {
                    case "DEPOSIT":
                        if (parts.length == 3) {
                            String accountId = parts[1].trim();
                            double amount = Double.parseDouble(parts[2].trim());
                            BankAccount account = accounts.get(accountId);
                            if (account != null) {
                                account.deposit(amount);
                            }
                        }
                        break;
                    case "WITHDRAW":
                        if (parts.length == 3) {
                            String accountId = parts[1].trim();
                            double amount = Double.parseDouble(parts[2].trim());
                            BankAccount account = accounts.get(accountId);
                            if (account != null) {
                                account.withdraw(amount);
                            }
                        }
                        break;
                    case "TRANSFER":
                        if (parts.length == 4) {
                            String fromId = parts[1].trim();
                            String toId = parts[2].trim();
                            double amount = Double.parseDouble(parts[3].trim());
                            BankAccount fromAccount = accounts.get(fromId);
                            BankAccount toAccount = accounts.get(toId);
                            if (fromAccount != null && toAccount != null) {
                                fromAccount.transfer(toAccount, amount);
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing transactions: " + e.getMessage());
        }
    }
}