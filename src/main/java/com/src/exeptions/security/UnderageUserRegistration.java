package com.src.exeptions.security;

public class UnderageUserRegistration extends Exception{
    public String getMessage() {
        return "User must be at least 18 years old";
    }
}