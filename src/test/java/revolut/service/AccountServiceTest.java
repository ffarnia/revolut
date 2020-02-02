package revolut.service;

import com.revolut.challenge.exception.InvalidRequestException;
import com.revolut.challenge.exception.NotFoundException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.service.AccountService;
import com.revolut.challenge.service.AccountServiceImpl;
import com.revolut.challenge.service.TransferMoneyService;
import com.revolut.challenge.service.TransferMoneyServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by Fazel on 12/12/2019.
 */
public class AccountServiceTest {

    private AccountServiceImpl accountService;
    private TransferMoneyServiceImpl transferMoneyService;

    @Before
    public void init() {
        accountService = new AccountServiceImpl();
        transferMoneyService = new TransferMoneyServiceImpl();
    }

    @After
    public void preDestroy() {
        accountService.clearRepository();
        transferMoneyService.clearRepository();
    }

    @Test
    public void createNewAccountAndStoreInMemory_whenCorrectDataGiven() {
        Account givenAccount = createSampleAccount(100, 5000, "Fazel Farnia");
        accountService.saveOrUpdate(givenAccount);
    }

    @Test(expected = InvalidRequestException.class)
    public void createNewAccountAndStoreInMemory_whenInvalidDataGiven() {
        Account givenAccount = createSampleAccount(100, 0, "Fazel Farnia");
        accountService.saveOrUpdate(givenAccount);
    }

    @Test(expected = NotFoundException.class)
    public void checkRepositoryIsEmpty_whenNoAccountAdded() {
        accountService.loadAllAccounts();
    }

    @Test
    public void checkRepositoryItems_whenAccountsAdded() {
        accountService.saveOrUpdate(createSampleAccount(100, 5000, "Fazel Farnia"));
        accountService.saveOrUpdate(createSampleAccount(200, 6000, "Reza Koosha"));
        List<Account> accountListGiven = accountService.loadAllAccounts();
        assertFalse(accountListGiven.isEmpty());
        assertEquals(2, accountListGiven.size());
        assertEquals(100, accountListGiven.get(0).getAccountNumber().intValue());
        assertEquals(6000, accountListGiven.get(1).getBalance().intValue());
    }

    @Test
    public void transferMoneyBetweenAccounts_whenCorrectTransactionGiven() {
        prepareAccountsForTransfer();
        Transaction transactionGiven = createSampleTransaction(100, 200, 1);
        transferMoneyService.transferMoney(transactionGiven);
        List<Account> accounts = accountService.loadAllAccounts();
        assertEquals(3999, accounts.get(0).getBalance().intValue());
        assertEquals(7001, accounts.get(1).getBalance().intValue());

    }

    @Test(expected = InvalidRequestException.class)
    public void transferMoney_whenSameAccountNumberGiven() {
        prepareAccountsForTransfer();
        Transaction transactionGiven = createSampleTransaction(100, 100, 1);
        transferMoneyService.transferMoney(transactionGiven);
    }

    @Test(expected = NotFoundException.class)
    public void transferMoney_whenInvalidFromAccountNumberGiven() {
        prepareAccountsForTransfer();
        Transaction transactionGiven = createSampleTransaction(101, 200, 1);
        transferMoneyService.transferMoney(transactionGiven);
    }

    @Test(expected = NotFoundException.class)
    public void transferMoney_whenInvalidToAccountNumberGiven() {
        prepareAccountsForTransfer();
        Transaction transactionGiven = createSampleTransaction(100, 202, 1);
        transferMoneyService.transferMoney(transactionGiven);
    }

    @Test(expected = InvalidRequestException.class)
    public void transferMoney_whenWithdrawMoreThanBalanceGiven() {
        prepareAccountsForTransfer();
        Transaction transactionGiven = createSampleTransaction(100, 200, 4001);
        transferMoneyService.transferMoney(transactionGiven);
    }

    @Test(expected = InvalidRequestException.class)
    public void transferMoney_whenInvalidAmountGiven() {
        prepareAccountsForTransfer();
        Transaction transactionGiven = createSampleTransaction(100, 200, -1);
        transferMoneyService.transferMoney(transactionGiven);
    }

    @Test
    public void concurrent() throws ExecutionException, InterruptedException {
        prepareAccountsForTransferThead();
        int threads = 3000;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        List<Account> accounts = accountService.loadAllAccounts();
        assertEquals(4000, accounts.get(0).getBalance().intValue());
        for (int i = 0; i < threads; i++) {
            executorService.submit(() -> {
                Transaction transactionGiven = createSampleTransaction(100, 200, 1);
                Transaction transactionGiven2 = createSampleTransaction(200, 300, 2);
                transferMoneyService.transferMoney(transactionGiven);
                transferMoneyService.transferMoney(transactionGiven2);
            });
        }
        executorService.shutdown();
        executorService.awaitTermination(3, TimeUnit.SECONDS);
        assertEquals(4000, accounts.get(1).getBalance().intValue());
        assertEquals(16000, accounts.get(2).getBalance().intValue());
    }

    private Account createSampleAccount(Integer accountNumber, Integer balance, String ownerName) {
        Account account = new Account();
        account.setBalance(balance);
        account.setAccountNumber(accountNumber);
        account.setOwnerName(ownerName);
        return account;
    }

    private Transaction createSampleTransaction(Integer fromAcc, Integer toAcc, Integer amount) {
        Transaction transaction = new Transaction();
        transaction.setFromAccountNumber(fromAcc);
        transaction.setToAccountNumber(toAcc);
        transaction.setAmount(amount);
        return transaction;
    }

    private void prepareAccountsForTransfer() {
        Account fromAccountGiven = createSampleAccount(100, 4000, "Fazel Farnia");
        Account toAccountGiven = createSampleAccount(200, 7000, "Ali Razeghi");
        accountService.saveOrUpdate(fromAccountGiven);
        accountService.saveOrUpdate(toAccountGiven);
    }

    private void prepareAccountsForTransferThead() {
        prepareAccountsForTransfer();
        Account anotherAccountGiven = createSampleAccount(300, 10000, "Hajesmal");
        accountService.saveOrUpdate(anotherAccountGiven);
    }
}
