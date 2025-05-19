package Banking;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BankingMain {
    public List<BankAccount> calculate() {
        // Initialize four bank accounts with an initial balance (e.g., 20000)
        List<BankAccount> accountsList = new ArrayList<>();
        Map<String, BankAccount> accountsMap = new HashMap<>();

        // Create accounts and add to both list and map
        for (int i = 1; i <= 4; i++) {
            String accountId = String.valueOf(i);
            BankAccount account = new BankAccount(accountId, 20000);
            accountsList.add(account);
            accountsMap.put(accountId, account);
        }

        // Create threads to process transactions from each file
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            // Files are in the resources directory as seen in the project structure
            String fileName = "src/main/resources/" + i + ".txt";
            System.out.println("Processing file: " + fileName);
            TransactionProcessor processor = new TransactionProcessor(accountsMap);
            Thread thread = new Thread(() -> processor.processTransactions(fileName));
            threads.add(thread);
            thread.start();
        }

        // Wait for all threads to complete
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return accountsList;
    }

    public static void main(String[] args) {
        BankingMain main = new BankingMain();
        List<BankAccount> accounts = main.calculate();
        for (BankAccount account : accounts) {
            System.out.println("Final balance of Account Number " + account.getAccountId() + " : " + account.getBalance());
        }
    }
}