package com.src.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRegistration {
    private String username;

    private String fullName;

    private Date birthDate;

    private String password;
}