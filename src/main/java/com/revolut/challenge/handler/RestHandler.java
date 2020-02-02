package com.revolut.challenge.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.challenge.config.RevolutConfig;
import com.revolut.challenge.exception.RevolutException;
import com.revolut.challenge.model.Account;
import com.revolut.challenge.model.ResponseError;
import com.revolut.challenge.model.ResponseStatus;
import com.revolut.challenge.model.Transaction;
import com.revolut.challenge.service.AccountServiceImpl;
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

    public static final ObjectMapper mapper = new ObjectMapper();

    public static void sendResponse(HttpExchange exchange, Object responseEntity, int responseStatusCode) throws IOException {
        byte[] response = mapper.writeValueAsBytes(responseEntity);
        exchange.getResponseHeaders().set(RevolutConfig.CONTENT_TYPE, RevolutConfig.CONTENT_TYPE_VALUE);
        exchange.sendResponseHeaders(responseStatusCode, response.length);
        OutputStream output = exchange.getResponseBody();
        output.write(response);
        output.flush();
        exchange.close();
    }

}
