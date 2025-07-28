package com.src.controllers;

import com.src.models.BankAccount;
import com.src.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.List;

@RestController
@RequestMapping("/secured")
public class AccountController {
    private final AccountService bankAccountService;

    public AccountController(AccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/account")
    public ResponseEntity<?> getAllAccountsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<BankAccount> allBankAccountsForUser = bankAccountService.getAllBankAccountsForUser(authentication);

        return ResponseEntity.ok(allBankAccountsForUser);
    }

    @PostMapping("/account")
    public ResponseEntity<?> addAccountForUser(@RequestParam("currency") Currency currency) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BankAccount newBankAccount = bankAccountService.addBankAccount(authentication, currency);

        return ResponseEntity.ok(newBankAccount);
    }

    @GetMapping("/account/{userId}")
    public ResponseEntity<?> getAccountById(@PathVariable("userId") String userId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        BankAccount userBankAccount = bankAccountService.bankAccount(userId, authentication);

        return ResponseEntity.ok(userBankAccount);
    }
}
