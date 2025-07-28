package com.src.services;

import com.src.models.BankAccount;
import com.src.repositorys.BankAccountRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankAccountService {
    private final BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount bankAccount(String accountId) {
        Optional<BankAccount> user = bankAccountRepository.findById(accountId);

        if (user.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return user.get();
    }

    public List<BankAccount> getAllBankAccountsForUser(String userId) {
        return bankAccountRepository.getAllByUserId(userId);
    }

    public BankAccount addBankAccount(String userId) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserId(userId);

        Date date = new Date();

        bankAccount.setCreatedAt(date);
        bankAccount.setUpdatedAt(date);
        bankAccountRepository.save(bankAccount);

        return bankAccount;
    }
}