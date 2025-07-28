package com.src.exeptions.account;

public class NoReceiverOrSenderAccountException extends Exception {
    public String getMessage() {
        return "No receiver or sender account";
    }
}
