import app.account.Account;
import org.junit.After;
import org.junit.Test;

import static app.service.AccountsService.INSTANCE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * * FIXME: cover negative cases
 */
public class AccountsServiceTest {

    @After
    public void after() {
        INSTANCE.clear();
    }

    @Test
    public void createAccountTest() {
        Account acc = INSTANCE.createAccount(5000d);
        assertEquals(5000d, acc.getValue(), 0);
    }

    @Test
    public void getAccountTest() {
        Account acc = INSTANCE.createAccount(5000d);
        assertEquals(acc, INSTANCE.getAccount(acc.getId()));
    }

    @Test
    public void getAllAccountsCopiedTest() {
        Account acc = INSTANCE.createAccount(5000d);
        Account acc1 = INSTANCE.createAccount(1000d);
        assertArrayEquals(new Account[] {acc, acc1}, INSTANCE.getAllAccountsCopied().toArray(new Account[0]));
    }

    @Test
    public void isAccountExistsTest() {
        Account acc = INSTANCE.createAccount(5000d);
        assertTrue(INSTANCE.isAccountExists(acc.getId()));
    }

    @Test
    public void changeAccountTest() {
        Account acc = INSTANCE.createAccount(5000d);
        assertEquals(1000d, INSTANCE.changeAccount(acc.getId(), 1000d).getValue(), 0d);
    }

    @Test
    public void deleteAccountTest() {
        Account acc = INSTANCE.createAccount(5000d);
        INSTANCE.deleteAccount(acc.getId());
        assertTrue(INSTANCE.getAllAccountsCopied().isEmpty());
    }

    @Test
    public void transferTest() {
        Account to = INSTANCE.createAccount(3000d);
        Account from = INSTANCE.createAccount(5000d);
        INSTANCE.transfer(from.getId(), to.getId(), 1000d);
        assertEquals(4000d, INSTANCE.getAccount(to.getId()).getValue(), 0d);
        assertEquals(4000d, INSTANCE.getAccount(from.getId()).getValue(), 0d);
    }
}
