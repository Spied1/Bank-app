package com.src.services;

import com.src.components.UserDetailsImpl;
import com.src.models.Account;
import com.src.models.DTO.AccountCreationInformation;
import com.src.repositorys.AccountRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository bankAccountRepository;

    public AccountService(AccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public Account getAccountInformationById(String accountId, Authentication authentication) {
        Optional<Account> account = bankAccountRepository.findById(accountId);

        if (account.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (!Objects.equals(account.get().getUserId(), getUserId(authentication))) {
            throw new IllegalArgumentException();
        }

        return account.get();
    }

    public List<Account> getAllBankAccountsForUser(Authentication authentication) {
        String userId = getUserId(authentication);

        return bankAccountRepository.getAllByUserId(userId);
    }

    public Account addBankAccount(Authentication authentication, AccountCreationInformation accountCreationInformation) {
        Account account = new Account();
        account.setUserId(getUserId(authentication));

        account.setName(accountCreationInformation.getName());
        account.setCurrency(accountCreationInformation.getCurrency());
        Date date = new Date();

        account.setCreatedAt(date);
        account.setUpdatedAt(date);
        bankAccountRepository.save(account);

        return account;
    }

    private String getUserId(Authentication authentication) {
        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetailsImpl)) {
            throw new AuthorizationServiceException("No such user");
        }

        return ((UserDetailsImpl) principal).getId();
    }

    @Transactional
    public void sendMoney(Authentication authentication, String senderAccountId,
                          String receiverAccountId, int moneyToSend) {
        String senderId = getUserId(authentication);

        if (moneyToSend <= 0) {
            throw new IllegalArgumentException();
        }

        Optional<Account> senderAccount = bankAccountRepository.findById(senderAccountId);
        Optional<Account> receiverAccount = bankAccountRepository.findById(receiverAccountId);

        if (senderAccount.isEmpty() || receiverAccount.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (senderAccount.get().getBalance() <= moneyToSend) {
            throw new IllegalArgumentException();
        }

        if (senderAccount.get().getCurrency() != receiverAccount.get().getCurrency()) {
            throw new IllegalArgumentException();
        }

        senderAccount.get().setBalance(senderAccount.get().getBalance() - moneyToSend);
        receiverAccount.get().setBalance(receiverAccount.get().getBalance() + moneyToSend);

        bankAccountRepository.save(senderAccount.get());
        bankAccountRepository.save(receiverAccount.get());
    }
}