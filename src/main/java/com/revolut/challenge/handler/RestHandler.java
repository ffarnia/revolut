package com.revolut.challenge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.challenge.config.RevolutConfig;
import com.revolut.challenge.exception.RevolutException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ResponseError;
import com.revolut.challenge.model.ResponseStatus;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.service.ServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @author Fazel Farnia
 *         Provide all rest endpoints and send appropriate response to client
 */
public class RestHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ServiceImpl service = new ServiceImpl();

    /**
     * Create and update Account entity by post request
     *
     * @return HttpHandler
     */
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
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()), ResponseStatus.INTERNAL_ERROR.getCode());
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
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()), ResponseStatus.INTERNAL_ERROR.getCode());
                }
                sendResponse(exchange, accounts, ResponseStatus.OK.getCode());
            } else {
                exchange.sendResponseHeaders(ResponseStatus.METHOD_NOT_ALLOWED.getCode(), -1);
            }
            exchange.close();
        };
    }

    /**
     * Transfer money between accounts with specified Transaction entity provided by post request
     * This method is thread safe for concurrent calls
     *
     * @return HttpHandler
     */
    public static HttpHandler handleTransferMoney() {
        return exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase(RevolutConfig.REQUEST_METHOD_POST)) {
                Transaction requestTransaction = null;
                try {
                    requestTransaction = mapper.readValue(exchange.getRequestBody(), Transaction.class);
                } catch (IOException e) {
                    exchange.sendResponseHeaders(ResponseStatus.BAD_REQUEST.getCode(), -1);
                }
                try {
                    service.transferMoney(requestTransaction);
                } catch (RevolutException e) {
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()), ResponseStatus.INTERNAL_ERROR.getCode());
                }
                exchange.sendResponseHeaders(ResponseStatus.OK.getCode(), -1);
            } else {
                exchange.sendResponseHeaders(ResponseStatus.METHOD_NOT_ALLOWED.getCode(), -1);
            }
            exchange.close();
        };
    }

    private static void sendResponse(HttpExchange exchange, Object responseEntity, int responseStatusCode) throws IOException {
        byte[] response = mapper.writeValueAsBytes(responseEntity);
        exchange.getResponseHeaders().set(RevolutConfig.CONTENT_TYPE, RevolutConfig.CONTENT_TYPE_VALUE);
        exchange.sendResponseHeaders(responseStatusCode, response.length);
        OutputStream output = exchange.getResponseBody();
        output.write(response);
        output.flush();
        exchange.close();
    }

}
