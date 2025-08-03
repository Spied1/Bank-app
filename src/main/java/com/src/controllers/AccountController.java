package com.src.controllers;

import com.src.exeptions.account.*;
import com.src.exeptions.user.NoUserFound;
import com.src.models.Account;
import com.src.models.DTO.AccountCreationInformation;
import com.src.models.Transfer;
import com.src.services.AccountService;
import org.springframework.http.ResponseEntity;
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
        try {
            List<Account> allBankAccountsForUser = accountService.getAllBankAccountsForUser();

            return ResponseEntity.ok(allBankAccountsForUser);
        } catch (NoUserFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/account")
    public ResponseEntity<?> addAccountForUser(@RequestBody AccountCreationInformation accountCreationInformation) {
        try {
            Account newBankAccount = accountService.addBankAccount(accountCreationInformation);

            return ResponseEntity.ok(newBankAccount);
        } catch (NoUserFound e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable("accountId") String accountId) {
        try {
            Account userAccount = accountService.getAccountInformationById(accountId);

            return ResponseEntity.ok(userAccount);
        } catch (NoAccountWithGivenParameters | WrongUserForAccount e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/account/send-money")
    public ResponseEntity<?> sendMoney(@RequestParam("senderId") String senderId,
                                       @RequestParam("receiverId") String receiverId,
                                       @RequestParam("moneyToSend") int moneyToSend) {
        try {
            accountService.sendMoney(senderId, receiverId, moneyToSend);

            return ResponseEntity.ok().build();
        } catch (WrongAmountOfMoneyException | NotEnoughMoneyException | NoReceiverOrSenderAccountException |
                 MismatchOfCurrenciesException | WrongUserForAccount e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/transfer-history/{accountId}")
    public ResponseEntity<?> getAllTransfersForAccount(@PathVariable("accountId") String accountId) {
        try {
            List<Transfer> transfers = accountService.getAllTransfersByAccount(accountId);

            return ResponseEntity.ok(transfers);
        } catch (WrongUserForAccount e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}