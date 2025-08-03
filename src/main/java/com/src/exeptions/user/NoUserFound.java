package com.src.exeptions.user;

public class NoUserFound extends Exception {
    public String getMessage() {
        return "No user found";
    }
}