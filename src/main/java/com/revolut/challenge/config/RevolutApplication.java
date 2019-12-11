package com.revolut.challenge.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.service.ServiceImpl;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by Fazel on 12/11/2019.
 */
public class RevolutApplication {

    private static final int SERVER_PORT = 8085;
    private static final String CREATE_ACCOUNT_ENDPOINT = "/api/account/create";
    private static final String LOAD_ALL_ACCOUNT_ENDPOINT = "/api/account/loadall";
    private static final String TRANSFER_MONEY_ENDPOINT = "/api/transaction/transferMoney";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final ServiceImpl service = new ServiceImpl();

    public static void main(String[] args) {
        HttpServer embeddedServer = null;
        try {
            embeddedServer = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (embeddedServer != null) {
            HttpContext context =  embeddedServer.createContext(CREATE_ACCOUNT_ENDPOINT, (exchange -> {
                Account requestAccount = null;
                try {
                    requestAccount = mapper.readValue(exchange.getRequestBody(), Account.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                service.saveOrUpdate(requestAccount);
                exchange.sendResponseHeaders(200, "that's ok".getBytes().length);
                OutputStream output = exchange.getResponseBody();
                output.write("that's ok".getBytes());
                output.flush();
                exchange.close();
            }));

            embeddedServer.createContext(LOAD_ALL_ACCOUNT_ENDPOINT, (exchange -> {
                List<Account> accounts =  service.fetchAllAccounts();
                exchange.sendResponseHeaders(200, mapper.writeValueAsBytes(accounts).length);
                OutputStream output = exchange.getResponseBody();
                output.write(mapper.writeValueAsBytes(accounts));
                output.flush();
                exchange.close();
            }));

            embeddedServer.createContext(TRANSFER_MONEY_ENDPOINT, (exchange -> {
               Transaction requestTransaction = mapper.readValue(exchange.getRequestBody(), Transaction.class);
                service.transferMoney(requestTransaction);
                exchange.sendResponseHeaders(200, -1);
                exchange.close();
            }));
        }
        embeddedServer.setExecutor(null);
        embeddedServer.start();
        System.out.println("Server started");
    }
}
