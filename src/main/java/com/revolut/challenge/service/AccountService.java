package com.revolut.challenge.service;

import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.Transaction;

import java.util.List;

/**
 * @author Fazel Farnia
 */
public interface AccountService {

    void saveOrUpdate(Account account);

    List<Account> loadAllAccounts();

}
