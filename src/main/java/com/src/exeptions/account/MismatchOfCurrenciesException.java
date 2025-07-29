package com.src.exeptions.account;

public class MismatchOfCurrenciesException extends Exception {
    public String getMessage() {
        return "Mismatch of receiver and sender currencies";
    }
}