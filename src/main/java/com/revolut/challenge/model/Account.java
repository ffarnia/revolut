package com.revolut.challenge.model;

/**
 * Created by Fazel on 12/11/2019.
 */
public class Account {

    private Integer accountNumber;
    private String ownerName;
    private Integer balance;

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }
}
