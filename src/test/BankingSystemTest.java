package Banking;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

public class BankingSystemTest {
    private static List<BankAccount> bankAccounts;
    private static BankingMain bankingMain;

    @BeforeAll
    public static void setUp() {
        bankingMain = new BankingMain();
        bankAccounts = bankingMain.calculate();
    }

    @Test
    public void testAccount1Balance() {
        assertEquals(18900.0, bankAccounts.get(0).getBalance(), 0.01, "Balance mismatch for account 1");
    }

    @Test
    public void testAccount2Balance() {
        assertEquals(27170.0, bankAccounts.get(1).getBalance(), 0.01, "Balance mismatch for account 2");
    }

    @Test
    public void testAccount3Balance() {
        assertEquals(21400.0, bankAccounts.get(2).getBalance(), 0.01, "Balance mismatch for account 3");
    }

    @Test
    public void testAccount4Balance() {
        assertEquals(10010.0, bankAccounts.get(3).getBalance(), 0.01, "Balance mismatch for account 4");
    }
}