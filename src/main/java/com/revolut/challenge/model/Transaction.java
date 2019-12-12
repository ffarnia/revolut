package com.revolut.challenge.model;

/**
 * Created by Fazel on 12/11/2019.
 */
public class Transaction {

    private Integer fromAccountNumber;
    private Integer toAccountNumber;
    private Integer amount;

    public Integer getFromAccountNumber() {
        return fromAccountNumber;
    }

    public void setFromAccountNumber(Integer fromAccountNumber) {
        this.fromAccountNumber = fromAccountNumber;
    }

    public Integer getToAccountNumber() {
        return toAccountNumber;
    }

    public void setToAccountNumber(Integer toAccountNumber) {
        this.toAccountNumber = toAccountNumber;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }
}
