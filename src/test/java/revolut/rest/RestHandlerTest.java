package revolut.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.challenge.config.RevolutApplication;
import com.revolut.challenge.config.RevolutConfig;
import com.revolut.challenge.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * Created by Fazel on 12/12/2019.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RestHandlerTest {

    private static final String BASE_URL = "http://127.0.0.1:8085";
    private static final Logger LOGGER = Logger.getLogger(RestHandlerTest.class.getName());
    private static ObjectMapper mapper = new ObjectMapper();
    private static HttpURLConnection conn;

    @Before
    public void init() {
        RevolutApplication.initalizeSerevr();
    }

    @Test
    public void a_createNewAccount() throws IOException {
        Account account = createSampleAccount(100, 3000, "Fazel Farnia");
        doRequest(RevolutConfig.CREATE_ACCOUNT_ENDPOINT, createRequestJsonBody(account), RevolutConfig.REQUEST_METHOD_POST);
        assertEquals(ResponseStatus.CREATED.getCode(), conn.getResponseCode());
    }

    @Test
    public void a_createAccountSecond() throws IOException {
        Account account = createSampleAccount(200, 7000, "Ostad Makan");
        doRequest(RevolutConfig.CREATE_ACCOUNT_ENDPOINT, createRequestJsonBody(account), RevolutConfig.REQUEST_METHOD_POST);
        assertEquals(ResponseStatus.CREATED.getCode(), conn.getResponseCode());
    }

    @Test
    public void checkRepositoryItems() throws IOException {
        doRequest(RevolutConfig.LOAD_ALL_ACCOUNT_ENDPOINT, "", RevolutConfig.REQUEST_METHOD_GET);
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
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(), conn.getResponseCode());
        ResponseError response = createResponseErrorEntity(conn.getErrorStream());
        assertEquals(104, response.getErrorCode());
        assertEquals(ConstantMessage.BOTH_ACCOUNT_NUMBER_ARE_SAME, response.getErrorMessage());
    }

    @Test
    public void transferMoney_whenInvalidFromAccountNumberGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(101, 200, 1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(), conn.getResponseCode());
        ResponseError response = createResponseErrorEntity(conn.getErrorStream());
        assertEquals(105, response.getErrorCode());
        assertEquals(ConstantMessage.FROM_ACCOUNT_NUMBER_NOT_FOUND, response.getErrorMessage());
    }

    @Test
    public void transferMoney_whenInvalidToAccountNumberGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 202, 1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(), conn.getResponseCode());
        ResponseError response = createResponseErrorEntity(conn.getErrorStream());
        assertEquals(106, response.getErrorCode());
        assertEquals(ConstantMessage.TO_ACCOUNT_NUMBER_NOT_FOUND, response.getErrorMessage());
    }

    @Test
    public void transferMoney_whenWithdrawMoreThanBalanceGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 200, 4001);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(), conn.getResponseCode());
        ResponseError response = createResponseErrorEntity(conn.getErrorStream());
        assertEquals(107, response.getErrorCode());
        assertEquals(ConstantMessage.WITHDRAW_MORE_THAN_BALANCE, response.getErrorMessage());
    }

    @Test
    public void transferMoney_whenInvalidAmountGiven() throws IOException {
        Transaction transactionGiven = createSampleTransaction(100, 200, -1);
        postEntityTransactionForMoneyTransfer(transactionGiven);
        assertEquals(ResponseStatus.INTERNAL_ERROR.getCode(), conn.getResponseCode());
        ResponseError response = createResponseErrorEntity(conn.getErrorStream());
        assertEquals(103, response.getErrorCode());
        assertEquals(ConstantMessage.TRANSFER_AMOUNT_MORE_THAN_ZERO, response.getErrorMessage());
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
        return mapper.writeValueAsString(input);
    }

    private void postEntityTransactionForMoneyTransfer(Transaction transaction) throws JsonProcessingException {
        doRequest(RevolutConfig.TRANSFER_MONEY_ENDPOINT, createRequestJsonBody(transaction), RevolutConfig.REQUEST_METHOD_POST);
    }

    private void doRequest(String endPoint, String input, String requestMethod) {
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
            LOGGER.log(Level.SEVERE, "Error in request sending", e.getMessage());
            conn.disconnect();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in request sending", e.getMessage());
            conn.disconnect();
        }
    }

    private ResponseError createResponseErrorEntity(InputStream inputStream) throws IOException {
        return mapper.readValue(inputStream, ResponseError.class);
    }

    @After
    public void tearDownd() {
        conn.disconnect();
        RevolutApplication.shutDownServer();
    }
}
