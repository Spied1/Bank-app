package com.src.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "id", columnDefinition = "VARCHAR(36)")
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    private String name;

    private Currency currency;

    private int balance;

    @OneToMany(mappedBy = "senderAccount")
    private List<Transfer> outgoingTransfers = new ArrayList<>();

    @OneToMany(mappedBy = "receiverAccount")
    private List<Transfer> incomingTransfers = new ArrayList<>();

    private Date createdAt;

    private Date updatedAt;
}