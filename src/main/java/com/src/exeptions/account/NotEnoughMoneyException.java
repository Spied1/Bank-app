package com.src.exeptions.account;

public class NotEnoughMoneyException extends Exception {
    public String getMessage() {
        return "Not enough money for transfer";
    }
}
