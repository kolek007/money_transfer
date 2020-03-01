package app.account.impl;


public class AccountDTO {
    long id;
    double value;

    public AccountDTO() {
    }

    public long getId() {
        return this.id;
    }

    public double getValue() {
        return this.value;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
