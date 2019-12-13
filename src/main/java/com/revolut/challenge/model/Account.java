package com.revolut.challenge.model;

/**
 * @author Fazel Farnia
 */
public class Account {

    private Integer accountNumber;
    private String ownerName;
    private Integer balance;

    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
            this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;

        Account account = (Account) o;

        if (getAccountNumber() != null ? !getAccountNumber().equals(account.getAccountNumber()) : account.getAccountNumber() != null)
            return false;
        if (getOwnerName() != null ? !getOwnerName().equals(account.getOwnerName()) : account.getOwnerName() != null)
            return false;
        return !(getBalance() != null ? !getBalance().equals(account.getBalance()) : account.getBalance() != null);

    }

    @Override
    public int hashCode() {
        int result = getAccountNumber() != null ? getAccountNumber().hashCode() : 0;
        result = 31 * result + (getOwnerName() != null ? getOwnerName().hashCode() : 0);
        result = 31 * result + (getBalance() != null ? getBalance().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountNumber=" + accountNumber +
                ", ownerName='" + ownerName + '\'' +
                ", balance=" + balance +
                '}';
    }
}
