package com.revolut.challenge.config;

import com.revolut.challenge.handler.RestHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Fazel Farnia
 *         Revolut Application where start the server and ready all endpoints for client
 */
public class RevolutApplication {

    private static final Logger LOGGER = Logger.getLogger(RevolutApplication.class.getName());
    private static HttpServer embeddedServer;

    public static void main(String[] args) {
        initalizeSerevr();
    }

    public static void initalizeSerevr() {
        try {
            embeddedServer = HttpServer.create(new InetSocketAddress(RevolutConfig.SERVER_PORT), 0);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in creating Http Server");
        }
        if (embeddedServer != null) {
            embeddedServer.createContext(RevolutConfig.CREATE_ACCOUNT_ENDPOINT, RestHandler.handleCreateAccount());
            embeddedServer.createContext(RevolutConfig.LOAD_ALL_ACCOUNT_ENDPOINT, RestHandler.handleFetchAllAccount());
            embeddedServer.createContext(RevolutConfig.TRANSFER_MONEY_ENDPOINT, RestHandler.handleTransferMoney());
        }
        embeddedServer.setExecutor(null);
        embeddedServer.start();
        LOGGER.log(Level.INFO, "Embedded Server start successfully");
    }

    public static void shutDownServer() {
        embeddedServer.stop(1);
    }
}
