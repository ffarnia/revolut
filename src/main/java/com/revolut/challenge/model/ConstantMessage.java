package com.revolut.challenge.model;

/**
 * @author Fazel Farnia
 */
public class ConstantMessage {

    public static final String EMPTY_ACCOUNT_LIST = "There are not any account in repository, please add some valid accounts by rest endpoint.";
    public static final String BALANCE_MORE_THAN_ZERO = "The balance for creating new account should be more than zero";
    public static final String BOTH_ACCOUNT_NUMBER_ARE_SAME = "Source account number and target account number should not be the same. provide different ";
    public static final String FROM_ACCOUNT_NUMBER_NOT_FOUND = "Source account number can not be found in repository. please enter valid account number";
    public static final String TO_ACCOUNT_NUMBER_NOT_FOUND = "Target account number can not be found in repository. please enter valid account number";
    public static final String WITHDRAW_MORE_THAN_BALANCE = "You can not withdraw money from source account more than balance amount";
    public static final String TRANSFER_AMOUNT_MORE_THAN_ZERO = "The amount for transfering should be more than zero. please enter a valid and positive amount ";

}
