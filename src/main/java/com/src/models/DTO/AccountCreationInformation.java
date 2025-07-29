package com.src.models.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Currency;

@Getter
@Setter
public class AccountCreationInformation {
    private String name;

    private Currency currency;
}