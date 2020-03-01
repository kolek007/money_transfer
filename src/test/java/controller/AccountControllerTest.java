package controller;

import app.AccountsApplication;
import app.account.Account;
import app.account.impl.AccountDTO;
import app.service.AccountsService;
import com.despegar.http.client.*;
import com.despegar.sparkjava.test.SparkServer;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import spark.servlet.SparkApplication;

import static app.util.RequestUtil.toJson;
import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * FIXME: cover negative cases
 */
public class AccountControllerTest {

    public static class AccountControllerTestSparkApplication implements SparkApplication {

        @Override
        public void init() {
            AccountsApplication.main(new String[]{});
        }
    }

    @ClassRule
    public static SparkServer<AccountControllerTestSparkApplication> testServer = new SparkServer<>(AccountControllerTest.AccountControllerTestSparkApplication.class, 8080);

    @After
    public void after() {
        AccountsService.INSTANCE.clear();
    }

    @Test
    public void checkInitTest() throws Exception {
        assertNotNull(testServer.getApplication());
    }

    @Test
    public void emptyGetTest() throws Exception {
		/* The second parameter indicates whether redirects must be followed or not */
        GetMethod get = testServer.get("/get", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals("[ ]", new String(httpResponse.body()));
    }

    @Test
    public void createNewAccountTest() throws Exception {
        AccountDTO account = account(1, 5000);
        PostMethod post = testServer.post("/new", toJson(account), false);
        HttpResponse httpResponse = testServer.execute(post);
        assertEquals(200, httpResponse.code());
        account.setId(2);
        assertEquals(toJson(account), new String(httpResponse.body()));
    }

    @Test
    public void getTest() throws Exception {
        Account acc = AccountsService.INSTANCE.createAccount(5000);

        GetMethod get = testServer.get("/get", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals("[ " + toJson(acc) + " ]", new String(httpResponse.body()));
    }

    @Test
    public void get2Test() throws Exception {
        Account acc = AccountsService.INSTANCE.createAccount(5000);
        Account acc2 = AccountsService.INSTANCE.createAccount(1000);

        GetMethod get = testServer.get("/get", false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals("[ " +
                toJson(acc) +
                ", " +
                toJson(acc2) +
                " ]", new String(httpResponse.body()));
    }

    @Test
    public void getByIdTest() throws Exception {
        Account acc = AccountsService.INSTANCE.createAccount(5000);

        GetMethod get = testServer.get("/get/" + 2, false);
        HttpResponse httpResponse = testServer.execute(get);
        assertEquals(200, httpResponse.code());
        assertEquals(toJson(acc), new String(httpResponse.body()));
    }

    @Test
    public void updateTest() throws Exception {
        AccountsService.INSTANCE.createAccount(5000);

        String body = toJson(account(2, 1000));
        PutMethod put = testServer.put("/update", body, false);
        HttpResponse httpResponse = testServer.execute(put);
        assertEquals(200, httpResponse.code());
        assertEquals(body, new String(httpResponse.body()));
        assertEquals(1000d, AccountsService.INSTANCE.getAccount(2).getValue(), 0d);
    }

    @Test
    public void deleteTest() throws Exception {
        Account acc = AccountsService.INSTANCE.createAccount(5000);

        DeleteMethod delete = testServer.delete("/delete/" + 2, false);
        HttpResponse httpResponse = testServer.execute(delete);
        assertEquals(200, httpResponse.code());
        assertEquals(toJson(acc), new String(httpResponse.body()));
        assertEquals(0, AccountsService.INSTANCE.getAllAccountsCopied().size());
    }

    @Test
    public void transferTest() throws Exception {
        Account from = AccountsService.INSTANCE.createAccount(5000);
        Account to = AccountsService.INSTANCE.createAccount(1000);

        PutMethod put = testServer.put("/transfer/2/3/2000", "", false);
        HttpResponse httpResponse = testServer.execute(put);
        assertEquals(200, httpResponse.code());

        assertEquals(3000d, AccountsService.INSTANCE.getAccount(2).getValue(), 0d);
        assertEquals(3000d, AccountsService.INSTANCE.getAccount(3).getValue(), 0d);
    }

    private static AccountDTO account(long id, double value) {
        AccountDTO acc = new AccountDTO();
        acc.setId(id);
        acc.setValue(value);
        return acc;
    }
}
