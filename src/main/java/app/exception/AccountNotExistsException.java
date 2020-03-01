package app.exception;

import app.account.Account;

/**
 * Unchecked exception, should thrown when can't find an {@link Account} object
 */
public class AccountNotExistsException extends RuntimeException {

    public AccountNotExistsException(String message) {
        super(message);
    }
}
