package app.exception;

import app.account.Account;

/**
 * Thrown if failed to change money amount of the {@link Account}
 */
public class AccountOperationException extends RuntimeException {

    public AccountOperationException(String message) {
        super(message);
    }
}
