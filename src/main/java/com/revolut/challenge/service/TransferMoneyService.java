package com.revolut.challenge.service;

import com.revolut.challenge.model.Transaction;

/**
 * Created by Fazel on 1/25/2020.
 */
public interface TransferMoneyService {

    void transferMoney(Transaction transaction);

}
