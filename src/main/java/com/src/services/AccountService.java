package com.src.services;

import com.src.components.UserDetailsImpl;
import com.src.models.BankAccount;
import com.src.repositorys.BankAccountRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AccountService {
    private final BankAccountRepository bankAccountRepository;

    public AccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount bankAccount(String accountId, Authentication authentication) {
        Optional<BankAccount> account = bankAccountRepository.findById(accountId);

        if (account.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (!Objects.equals(account.get().getUserId(), getUserId(authentication))) {
            throw new IllegalArgumentException();
        }

        return account.get();
    }

    public List<BankAccount> getAllBankAccountsForUser(Authentication authentication) {
        String userId = getUserId(authentication);

        return bankAccountRepository.getAllByUserId(userId);
    }

    public BankAccount addBankAccount(Authentication authentication, Currency currency) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setUserId(getUserId(authentication));

        bankAccount.setCurrency(currency);
        Date date = new Date();

        bankAccount.setCreatedAt(date);
        bankAccount.setUpdatedAt(date);
        bankAccountRepository.save(bankAccount);

        return bankAccount;
    }

    public String getUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl)) {
            throw new AuthorizationServiceException("No such user");
        }

        return ((UserDetailsImpl) principal).getId();
    }
}