package com.src.models.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class AccountInformationToSend {
    private String name;

    private Currency currency;

    private int balance;

    private Date createdAt;

    private Date updatedAt;
}