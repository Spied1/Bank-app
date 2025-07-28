package com.src.exeptions.account;

public class WrongAmountOfMoneyException extends Exception {
    public String getMessage() {
        return "Wrong amount of money exception";
    }
}
