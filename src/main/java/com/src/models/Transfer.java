package com.src.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Currency;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "transfers")
public class Transfer {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "transfer_id")
    private String transferId;

    @Column(name = "sender_user_id")
    private String senderUserId;

    @Column(name = "sender_account_id")
    private String senderAccountId;

    @Column(name = "receiver_user_id")
    private String receiverUserId;

    @Column(name = "receiver_account_id")
    private String receiverAccountId;

    @Column(name = "amount_of_money")
    private int amountOfMoney;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "time_of_transfer")
    private Date timeOfTransfer;
}