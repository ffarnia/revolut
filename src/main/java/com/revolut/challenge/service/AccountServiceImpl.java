package com.revolut.challenge.service;

import com.revolut.challenge.exception.InvalidRequestException;
import com.revolut.challenge.exception.NotFoundException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ConstantMessage;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.repository.InMemoryRepository;

import java.util.List;

/**
 * @author Fazel Farnia
 *         AccountService class responsible for implementing services for endpoints also implement business and logic
 */
public class AccountServiceImpl extends BaseServiceImpl implements AccountService {

    @Override
    public void saveOrUpdate(Account account) {
        if (account.getBalance() > 0) {
            repository.createAccount(account);
        } else {
            throw new InvalidRequestException(102, ConstantMessage.BALANCE_MORE_THAN_ZERO);
        }
    }

    @Override
    public List<Account> loadAllAccounts() {
        List<Account> accountList = repository.loadAllAccounts();
        if (accountList.isEmpty()) {
            throw new NotFoundException(101, ConstantMessage.EMPTY_ACCOUNT_LIST);
        } else return accountList;
    }

}
