package com.src.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "sender")
    @JsonIgnore
    private User sender;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    @JsonIgnore
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "receiver")
    @JsonIgnore
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    @JsonIgnore
    private Account receiverAccount;

    @Column(name = "amount_of_money")
    private int amountOfMoney;

    @Column(name = "currency")
    private Currency currency;

    @Column(name = "time_of_transfer")
    private Date timeOfTransfer;
}