package app.account.impl;

import app.account.Account;
import app.exception.AccountOperationException;
import app.service.AccountsService;

import java.util.Random;

import static app.util.AccountsUtil.validateAmount;

/**
 * Threadsafe implementation of  {@link Account}.
 * Shouldn't be passed outside of current service
 * Allows to update Account balance and transfer money to other account in a concurrent environment
 */
public class AccountImpl implements Account {
    private final long id;
    private volatile double value;

    private final Random random = new Random(System.currentTimeMillis());
    private final Object lock = new Object();

    /**
     * Constructor for Account, allows to setup id and current money balance
     */
    public AccountImpl(long id, double value) {
        this.id = id;
        this.value = value;
    }

    public AccountImpl(Account account) {
        this.id = account.getId();
        this.value = account.getValue();
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public double getValue() {
        return value;
    }

    public void updateValue(double newValue) {
        validateAmount(newValue);
        synchronized (lock) {
            this.value = newValue;
        }
    }

    /**
     * Transfers money from current {@link Account} to another
     * Should only be called from {@link AccountsService}
     */
    public void transfer(AccountImpl to, double amount) {
        if(id > to.id) {
            throw new AccountOperationException("Illegal method call. Method should be called only in case when id of source account is less then id of target otherwise deadlock may occur");
        }
        synchronized (lock) {
            synchronized (to.lock) {
                transferUnsafe(to, amount);
            }
        }
    }

    private void transferUnsafe(AccountImpl to, double amount) {
        validateAmount(this.value - amount);
        validateAmount(to.value + amount);

        to.value += amount; //direct accessing field because we already have write lock
        this.value -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountImpl account = (AccountImpl) o;

        return id == account.id;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }
}
