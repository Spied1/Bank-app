package com.src.exeptions.account;

public class WrongUserForAccount extends Exception {
    public String getMessage() {
        return "Wrong user for account";
    }
}