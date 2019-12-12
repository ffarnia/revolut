package com.revolut.challenge.service;

import com.revolut.challenge.exception.InvalidRequestException;
import com.revolut.challenge.exception.NotFoundException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ConstantMessage;
import com.revolut.challenge.exception.RevolutException;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.repository.InMemoryRepository;

import java.util.List;

/**
 * Created by Fazel on 12/11/2019.
 */
public class ServiceImpl implements Service {

    private final InMemoryRepository repository;

    public ServiceImpl() {
        repository = new InMemoryRepository();
    }

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

    @Override
    public void transferMoney(Transaction transaction) {
        validateRequestTransaction(transaction);
        Account fromAccount = repository.loadByAccountNumber(transaction.getFromAccountNumber());
        if (fromAccount == null) {
            throw new NotFoundException(105, ConstantMessage.FROM_ACCOUNT_NUMBER_NOT_FOUND);
        }
        Account toAccount = repository.loadByAccountNumber(transaction.getToAccountNumber());
        if (toAccount == null) {
            throw new NotFoundException(106, ConstantMessage.TO_ACCOUNT_NUMBER_NOT_FOUND);
        }
        if (fromAccount.getBalance().intValue() < transaction.getAmount()) {
            throw new InvalidRequestException(107, ConstantMessage.WITHDRAW_MORE_THAN_BALANCE);
        }
        synchronized (this) {
            fromAccount.setBalance(fromAccount.getBalance() - transaction.getAmount());
            toAccount.setBalance(toAccount.getBalance() + transaction.getAmount());
            saveOrUpdate(fromAccount);
            saveOrUpdate(toAccount);
        }
    }

    private void validateRequestTransaction(Transaction requestTransaction) {
        if (requestTransaction.getAmount() <= 0) {
            throw new InvalidRequestException(103, ConstantMessage.TRANSFER_AMOUNT_MORE_THAN_ZERO);
        }
        if (requestTransaction.getFromAccountNumber().equals(requestTransaction.getToAccountNumber())) {
            throw new InvalidRequestException(104, ConstantMessage.BOTH_ACCOUNT_NUMBER_ARE_SAME);
        }
    }

}
