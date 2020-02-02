package com.revolut.challenge.handler;

import com.revolut.challenge.config.RevolutConfig;
import com.revolut.challenge.exception.RevolutException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ResponseError;
import com.revolut.challenge.model.ResponseStatus;
import com.revolut.challenge.service.AccountServiceImpl;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

/**
 * Created by Fazel on 1/25/2020.
 */
public class AccountRest extends RestHandler{

    public static final AccountServiceImpl service = new AccountServiceImpl();

    public static HttpHandler handleCreateAccount() {
        return exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase(RevolutConfig.REQUEST_METHOD_POST)) {
                Account requestAccount = null;
                try {
                    requestAccount = mapper.readValue(exchange.getRequestBody(), Account.class);
                } catch (IOException e) {
                    exchange.sendResponseHeaders(ResponseStatus.BAD_REQUEST.getCode(), -1);
                }
                try {
                    service.saveOrUpdate(requestAccount);
                } catch (RevolutException e) {
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()), ResponseStatus.UNPROCCESSABLE.getCode());
                }
                exchange.sendResponseHeaders(ResponseStatus.CREATED.getCode(), -1);
            } else {
                exchange.sendResponseHeaders(ResponseStatus.METHOD_NOT_ALLOWED.getCode(), -1);
            }
            exchange.close();
        };
    }

    /**
     * Load all stored accounts in repository and send a list of accounts by get request
     *
     * @return HttpHandler
     */
    public static HttpHandler handleFetchAllAccount() {
        return exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase(RevolutConfig.REQUEST_METHOD_GET)) {
                List<Account> accounts = null;
                try {
                    accounts = service.loadAllAccounts();
                } catch (RevolutException e) {
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()), ResponseStatus.UNPROCCESSABLE.getCode());
                }
                sendResponse(exchange, accounts, ResponseStatus.OK.getCode());
            } else {
                exchange.sendResponseHeaders(ResponseStatus.METHOD_NOT_ALLOWED.getCode(), -1);
            }
            exchange.close();
        };
    }

}
