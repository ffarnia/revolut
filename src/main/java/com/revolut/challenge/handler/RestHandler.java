package com.revolut.challenge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ResponseError;
import com.revolut.challenge.model.RevolutException;
import com.revolut.challenge.model.Transaction;
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
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                Account requestAccount = null;
                try {
                    requestAccount = mapper.readValue(exchange.getRequestBody(), Account.class);
                } catch (IOException e) {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, -1);
                }
                try {
                    service.saveOrUpdate(requestAccount);
                } catch (RevolutException e) {
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()));
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(201, -1); //Created
            } else {
                exchange.sendResponseHeaders(405, -1); //Method not allowed
            }
            exchange.close();
        };
    }

    public static HttpHandler handleFetchAllAccount() throws IOException {
        return exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {
                List<Account> accounts = null;
                try {
                    accounts = service.loadAllAccounts();
                } catch (RevolutException e) {
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()));
                }
                sendResponse(exchange, accounts);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        };

    }

    public static HttpHandler handleTransferMoney() {
        return exchange -> {
            if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {
                Transaction requestTransaction = null;
                try {
                    requestTransaction = mapper.readValue(exchange.getRequestBody(), Transaction.class);
                } catch (IOException e) {
                    exchange.getResponseHeaders().set("Content-Type", "application/json");
                    exchange.sendResponseHeaders(400, -1);
                }
                try {
                    service.transferMoney(requestTransaction);
                } catch (RevolutException e) {
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()));
                }
                exchange.getResponseHeaders().set("Content-Type", "application/json");
                exchange.sendResponseHeaders(200, -1);
            } else {
                exchange.sendResponseHeaders(405, -1);
            }
            exchange.close();
        };
    }

    private static void sendResponse(HttpExchange exchange, Object responseEntity) throws IOException {
        byte[] response = mapper.writeValueAsBytes(responseEntity);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, response.length);
        OutputStream output = exchange.getResponseBody();
        output.write(response);
        output.flush();
        exchange.close();
    }

}
