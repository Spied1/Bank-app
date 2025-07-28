package com.src.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class UserRegistrationDTO {
    private String username;

    private String name;

    private Date birthDate;

    private String password;
}
