package app.util;

import app.exception.NegativeAmountException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Class containing utility functions
 */
public class AccountsUtil {

    public static void validateAmount(double amount) {
        if(amount < 0d) {
            throw new NegativeAmountException("Trying to update an Account with negative amount of money" +
                    ". Only positive values are applicable");
        }
    }
}
