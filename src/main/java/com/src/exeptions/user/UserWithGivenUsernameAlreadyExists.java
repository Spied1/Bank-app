package com.src.exeptions.user;

public class UserWithGivenUsernameAlreadyExists extends Exception{
    public String getMessage() {
        return "User with given username already exists";
    }
}