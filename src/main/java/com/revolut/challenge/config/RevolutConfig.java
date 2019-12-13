package com.revolut.challenge.config;

/**
 * @author Fazel Farnia
 *         All hardcode configuration comes here it works similare to property file
 */
public class RevolutConfig {

    public static final int SERVER_PORT = 8085;
    public static final String CREATE_ACCOUNT_ENDPOINT = "/api/account/create";
    public static final String LOAD_ALL_ACCOUNT_ENDPOINT = "/api/account/loadall";
    public static final String TRANSFER_MONEY_ENDPOINT = "/api/transaction/transferMoney";
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_TYPE_VALUE = "application/json";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String REQUEST_METHOD_GET = "GET";
}
