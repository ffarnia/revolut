package com.revolut.challenge.repository;

import com.revolut.challenge.exception.InvalidRequestException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ConstantMessage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author Fazel Farnia
 *         Provide in memory datastructure for storing and retriving accounts
 */
public class InMemoryRepository {

    private final Map<Integer, Account> repository;

    public InMemoryRepository() {
        repository = new ConcurrentHashMap();
    }

    public void createAccount(Account account) {
        if (account.getBalance().intValue() > 0) {
            repository.put(account.getAccountNumber(), account);
        } else {
            throw new InvalidRequestException(102, ConstantMessage.BALANCE_MORE_THAN_ZERO);
        }
    }

    public List<Account> loadAllAccounts() {
        return repository.values().stream().collect(Collectors.toList());
    }

    public Account loadByAccountNumber(Integer accountNumber) {
        return repository.get(accountNumber);
    }
}
