package com.revolut.challenge.service;

import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.repository.InMemoryRepository;

import java.util.List;

/**
 * Created by Fazel on 12/11/2019.
 */
public class ServiceImpl {

    private final InMemoryRepository repository;

    public ServiceImpl() {
        repository = new InMemoryRepository();
    }

    public void saveOrUpdate(Account account) {
            repository.createAccount(account);
    }

    public List<Account> fetchAllAccounts() {
        return  repository.loadAllAccounts();
    }

    public Account fetchAccountByNumber(Integer accountNumber) {
        return repository.loadByAccountNumber(accountNumber);
    }

    public void transferMoney(Transaction transaction) {
        Account fromAccount = repository.loadByAccountNumber(transaction.getFromAccountNumber());
        Account toAccount = repository.loadByAccountNumber(transaction.getToAccountNumber());
        fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());
        saveOrUpdate(fromAccount);
        saveOrUpdate(toAccount);
    }

}
