package app.service;

import app.account.Account;
import app.account.impl.AccountImpl;
import app.exception.AccountNotExistsException;
import com.google.common.collect.Maps;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import static app.util.AccountsUtil.validateAmount;

/**
 * Threadsafe singleton allowing CRUD operations with Account objects
 */
public enum AccountsService {
    INSTANCE;

    private final AtomicLong nextId = new AtomicLong(1);
    private final Map<Long, AccountImpl> accounts = Maps.newConcurrentMap();

    /**
     * Creates and returns an Account with generated id and balance equal to {@code amount}
     */
    public Account createAccount(double amount) {
        validateAmount(amount);
        long id = nextId.incrementAndGet();
        AccountImpl account = new AccountImpl(id, amount);
        accounts.put(id, account);
        return account;
    }

    /**
     * Checks whether account exists or not
     */
    public boolean isAccountExists(long id) {
        return accounts.containsKey(id);
    }

    /**
     * Retrieves Account, if account not exists then returns {@link java.util.Optional#empty()}
     */
    @Nullable
    public Account getAccount(long id) {
        return accounts.get(id);
    }

    /**
     * Updates account balance to a {@code newAmount} value
     */
    @Nonnull
    public Account changeAccount(long id, double newAmount) {
        AccountImpl acc = getExistingAccount(id);
        acc.updateValue(newAmount);
        return acc;
    }

    /**
     * Deletes an Account and returns it
     */
    @Nullable
    public Account deleteAccount(long id) {
        return accounts.remove(id);

    }

    /**
     * Note, this method copies collection of accounts and this may lead to performance problems
     * @return new collection containing all available accounts
     */
    @Nonnull
    public Collection<Account> getAllAccountsCopied() {
        return accounts.values().stream().map(AccountImpl::new).collect(Collectors.toList());
    }

    /**
     * Transfers money from one {@link Account} to another.
     * Amount can have negative value
     * Equal source and target accounts are not allowed
     */
    public void transfer(long idFrom, long idTo, double amount) {
        if (idFrom == idTo) {
            throw new IllegalArgumentException("Account ids are equal. Transferring money is allowed only for different accounts, please pass not equal account ids.");
        }
        AccountImpl from = getExistingAccount(idFrom);
        AccountImpl to = getExistingAccount(idTo);

        //lets follow ordering when acquire locks to avoid deadlock
        if (idFrom > idTo) {
            to.transfer(from, -amount);
        } else {
            from.transfer(to, amount);
        }

    }


    /**
     * Returns an Account with {@code id}
     * {@link AccountNotExistsException} is thrown if Account with {@code id} wasn't found
     */
    private AccountImpl getExistingAccount(long id) {
        AccountImpl acc = accounts.get(id);
        if(acc == null) {
            throw new AccountNotExistsException("Couldn't change amount. Account with id '" + id + "' doesn't exists.");
        }
        return acc;
    }

    /*
        Cleans up service
     */
    public void clear() {
        accounts.clear();
        nextId.set(1);
    }
}
