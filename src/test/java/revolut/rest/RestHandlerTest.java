package revolut.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.challenge.config.RevolutApplication;
import com.revolut.challenge.config.RevolutConfig;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ResponseStatus;
import com.revolut.challenge.model.RevolutException;
import com.revolut.challenge.model.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Fazel on 12/12/2019.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestHandlerTest {

    private static final String BASE_URL = "http://127.0.0.1:8085";
    private static ObjectMapper mapper = new ObjectMapper();
    private static HttpURLConnection conn;

    @Before
    public void init() {
        RevolutApplication.initalizeSerevr();
    }

    @Test
    public void a_createNewAccount() throws IOException {
        Account account = createSampleAccount(100, 3000, "Fazel Farnia");
        doPost(RevolutConfig.CREATE_ACCOUNT_ENDPOINT, createRequestJsonBody(account), RevolutConfig.REQUEST_METHOD_POST);
        assertEquals(ResponseStatus.CREATED.getCode(), conn.getResponseCode());
    }

    @Test
    public void a_createAccountSecond() throws IOException {
        Account account = createSampleAccount(200, 7000, "Ostad Makan");
        doPost(RevolutConfig.CREATE_ACCOUNT_ENDPOINT, createRequestJsonBody(account), RevolutConfig.REQUEST_METHOD_POST);
        assertEquals(ResponseStatus.CREATED.getCode(), conn.getResponseCode());
    }

    @Test
    public void  checkRepositoryItems() throws IOException {
        doPost(RevolutConfig.LOAD_ALL_ACCOUNT_ENDPOINT, "", RevolutConfig.REQUEST_METHOD_GET);
        List<Account> accounts = mapper.readValue(conn.getInputStream(), ArrayList.class);
        assertEquals(ResponseStatus.OK.getCode(), conn.getResponseCode());
        assertEquals(2, accounts.size());
    }

    @Test
    public void transferMoneyBetweenAccounts_whenCorrectTransactionGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 200, 1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.OK.getCode(), conn.getResponseCode());
    }


    @Test
    public void transferMoney_whenSameAccountNumberGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 100, 1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(),conn.getResponseCode());
    }

    @Test
    public void transferMoney_whenInvalidFromAccountNumberGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(101, 200, 1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(),conn.getResponseCode());
    }

    @Test
    public void transferMoney_whenInvalidToAccountNumberGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 202, 1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(),conn.getResponseCode());
    }

    @Test
    public void transferMoney_whenWithdrawMoreThanBalanceGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 200, 4001);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(),conn.getResponseCode());
    }

    @Test
    public void transferMoney_whenInvalidAmountGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 200, -1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(),conn.getResponseCode());
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

    private String createRequestJsonBody(Object input) throws JsonProcessingException {
        return  mapper.writeValueAsString(input);
    }
    private void postEntityTransactionForMoneyTransfer(Transaction transaction) throws JsonProcessingException {
        doPost(RevolutConfig.TRANSFER_MONEY_ENDPOINT, createRequestJsonBody(transaction), RevolutConfig.REQUEST_METHOD_POST);
    }

    private void doPost(String endPoint, String input, String requestMethod) {
        try {
            URL url = new URL(BASE_URL + endPoint);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod(requestMethod);
            if (requestMethod.equalsIgnoreCase(RevolutConfig.REQUEST_METHOD_POST)) {
                conn.setRequestProperty(RevolutConfig.CONTENT_TYPE, RevolutConfig.CONTENT_TYPE_VALUE);
                OutputStream os = conn.getOutputStream();
                os.write(input.getBytes());
                os.flush();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            conn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            conn.disconnect();
        }
    }

    @After
    public void tearDownd() {
        conn.disconnect();
        RevolutApplication.shutDownServer();
    }
}
