package app.account;

/**
 * Created by nlyub on 27.02.2020.
 */
public interface Account {

    /**
     * Identifier of the Account
     */
    long getId();

    /**
     * Current money balance of the Account
     */
    double getValue();
}
