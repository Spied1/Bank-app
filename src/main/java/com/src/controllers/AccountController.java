package com.src.controllers;

import com.src.models.Account;
import com.src.models.DTO.AccountInformationToSend;
import com.src.models.DTO.AccountCreationInformation;
import com.src.services.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/secured")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService bankAccountService) {
        this.accountService = bankAccountService;
    }

    @GetMapping("/account")
    public ResponseEntity<?> getAllAccountsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<Account> allBankAccountsForUser = accountService.getAllBankAccountsForUser(authentication);

        return ResponseEntity.ok(allBankAccountsForUser);
    }

    @PostMapping("/account")
    public ResponseEntity<?> addAccountForUser(@RequestBody AccountCreationInformation accountCreationInformation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account newBankAccount = accountService.addBankAccount(authentication, accountCreationInformation);

        return ResponseEntity.ok(newBankAccount);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable("accountId") String accountId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Account userAccount = accountService.getAccountInformationById(accountId, authentication);

        return ResponseEntity.ok(userAccount);
    }

    @PostMapping("/account/send-money")
    public ResponseEntity<?> sendMoney(@RequestParam("senderId") String senderId,
                                       @RequestParam("receiverId") String receiverId,
                                       @RequestParam("moneyToSend") int moneyToSend) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        accountService.sendMoney(authentication, senderId, receiverId, moneyToSend);

        return ResponseEntity.ok().build();
    }
}
