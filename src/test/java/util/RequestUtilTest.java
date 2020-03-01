package util;

import app.account.impl.AccountDTO;
import app.util.RequestUtil;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 */
public class RequestUtilTest {

    @Test
    public void toJsonTest() {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(2);
        accountDTO.setValue(5000d);
        assertEquals("{\r\n" +
                        "  \"id\" : 2,\r\n" +
                        "  \"value\" : 5000.0\r\n" +
                        "}",
                RequestUtil.toJson(accountDTO));
    }

}
