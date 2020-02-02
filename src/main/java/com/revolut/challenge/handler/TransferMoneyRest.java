package com.revolut.challenge.handler;

import com.revolut.challenge.config.RevolutConfig;
import com.revolut.challenge.exception.RevolutException;
import com.revolut.challenge.model.ResponseError;
import com.revolut.challenge.model.ResponseStatus;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.service.TransferMoneyServiceImpl;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Created by Fazel on 1/25/2020.
 */
public class TransferMoneyRest extends RestHandler {

    public static final TransferMoneyServiceImpl service = new TransferMoneyServiceImpl();

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
                    sendResponse(exchange, new ResponseError(e.getCode(), e.getMessage()), ResponseStatus.UNPROCCESSABLE.getCode());
                }
                exchange.sendResponseHeaders(ResponseStatus.OK.getCode(), -1);
            } else {
                exchange.sendResponseHeaders(ResponseStatus.METHOD_NOT_ALLOWED.getCode(), -1);
            }
            exchange.close();
        };
    }

}
