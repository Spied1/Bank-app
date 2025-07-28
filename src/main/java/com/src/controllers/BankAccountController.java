package com.src.controllers;

import com.src.models.BankAccount;
import com.src.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("/account")
    public ResponseEntity<?> getAllAccountsForUser(@RequestHeader("userId") String userId) {
        List<BankAccount> allBankAccountsForUser = bankAccountService.getAllBankAccountsForUser(userId);

        return ResponseEntity.ok(allBankAccountsForUser);
    }

    @PostMapping("/account")
    public ResponseEntity<?> addAccountForUser(@RequestHeader("userId") String userId) {
        BankAccount newBankAccount = bankAccountService.addBankAccount(userId);

        return ResponseEntity.ok(newBankAccount);
    }

    @GetMapping("/account/{userId}")
    public ResponseEntity<?> getAccountById(@PathVariable("userId") String userId){
        BankAccount userBankAccount = bankAccountService.bankAccount(userId);

        return ResponseEntity.ok(userBankAccount);
    }
}
