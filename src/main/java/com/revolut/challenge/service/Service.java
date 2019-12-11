package com.revolut.challenge.service;

import com.revolut.challenge.model.Account;
import com.revolut.challenge.repository.InMemoryRepository;

import java.util.List;

/**
 * Created by Fazel on 12/11/2019.
 */
public class Service {

    private final InMemoryRepository repository;

    public Service() {
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

    public void transferMoney(Integer fromAccount, Integer toAccount, Integer amount) {

    }

}
