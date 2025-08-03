package com.src.exeptions.account;

public class NoAccountWithGivenParameters extends Exception {
    public String getMessage() {
        return "No account with give parameters";
    }
}