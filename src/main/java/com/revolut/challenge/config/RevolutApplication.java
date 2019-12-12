package com.revolut.challenge.config;

import com.revolut.challenge.handler.RestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Fazel on 12/11/2019.
 */
public class RevolutApplication {

    private static HttpServer embeddedServer;

    public static void main(String[] args) {
        initalizeSerevr();
    }

    public static void initalizeSerevr() {
        try {
            embeddedServer = HttpServer.create(new InetSocketAddress(RevolutConfig.SERVER_PORT), 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (embeddedServer != null) {
            embeddedServer.createContext(RevolutConfig.CREATE_ACCOUNT_ENDPOINT, RestHandler.handleCreateAccount());
            embeddedServer.createContext(RevolutConfig.LOAD_ALL_ACCOUNT_ENDPOINT, RestHandler.handleFetchAllAccount());
            embeddedServer.createContext(RevolutConfig.TRANSFER_MONEY_ENDPOINT, RestHandler.handleTransferMoney());
        }
        embeddedServer.setExecutor(null);
        embeddedServer.start();
    }

    public static void shutDownServer() {
        embeddedServer.stop(1);
    }
}
