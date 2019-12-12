package com.revolut.challenge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.challenge.config.RevolutConfig;
import com.revolut.challenge.exception.RevolutException;
import com.revolut.challenge.model.*;
import com.revolut.challenge.service.ServiceImpl;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by Fazel on 12/11/2019.
 */
public class RestHandler {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ServiceImpl service = new ServiceImpl();

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

    public static HttpHandler handleFetchAllAccount(){
        return exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase(RevolutConfig.REQUEST_METHOD_GET)) {
                List<Account> accounts = null;
                try {
                    accounts = service.loadAllAccounts();
                } catch (RevolutException e) {
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()), ResponseStatus.INTERNAL_ERROR.getCode());
                }
                sendResponse(exchange, accounts,ResponseStatus.OK.getCode());
            } else {
                exchange.sendResponseHeaders(ResponseStatus.METHOD_NOT_ALLOWED.getCode(), -1);
            }
            exchange.close();
        };
    }

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

    private static void sendResponse(HttpExchange exchange, Object responseEntity,int responseStatusCode) throws IOException {
        byte[] response = mapper.writeValueAsBytes(responseEntity);
        exchange.getResponseHeaders().set(RevolutConfig.CONTENT_TYPE, RevolutConfig.CONTENT_TYPE_VALUE);
        exchange.sendResponseHeaders(responseStatusCode, response.length);
        OutputStream output = exchange.getResponseBody();
        output.write(response);
        output.flush();
        exchange.close();
    }

}
