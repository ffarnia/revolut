package com.revolut.challenge.service;

import com.revolut.challenge.exception.InvalidRequestException;
import com.revolut.challenge.exception.NotFoundException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ConstantMessage;
import com.revolut.challenge.model.Transaction;

/**
 * Created by Fazel on 1/25/2020.
 */
public class TransferMoneyServiceImpl extends BaseServiceImpl implements TransferMoneyService {

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
            repository.createAccount(fromAccount);
            repository.createAccount(toAccount);
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
