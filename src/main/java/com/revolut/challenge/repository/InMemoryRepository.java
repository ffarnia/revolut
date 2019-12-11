package com.revolut.challenge.repository;

import com.revolut.challenge.model.Account;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Fazel on 12/11/2019.
 */
public class InMemoryRepository {

    private final Map<Integer, Account> repository;

    public InMemoryRepository() {
        repository = new ConcurrentHashMap();
    }

    public void createAccount(Account account) {
        repository.put(account.getAccountNumber(), account);
    }

    public List<Account> loadAllAccounts() {
        return repository.values().stream().collect(Collectors.toList());
    }

    public Account loadByAccountNumber(Integer accountNumber) {
        return repository.get(accountNumber);
    }
}
