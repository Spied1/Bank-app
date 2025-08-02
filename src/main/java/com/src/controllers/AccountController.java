package com.src.controllers;

import com.src.exeptions.account.MismatchOfCurrenciesException;
import com.src.exeptions.account.NoReceiverOrSenderAccountException;
import com.src.exeptions.account.NotEnoughMoneyException;
import com.src.exeptions.account.WrongAmountOfMoneyException;
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
        List<Account> allBankAccountsForUser = accountService.getAllBankAccountsForUser();

        return ResponseEntity.ok(allBankAccountsForUser);
    }

    @PostMapping("/account")
    public ResponseEntity<?> addAccountForUser(@RequestBody AccountCreationInformation accountCreationInformation) {
        Account newBankAccount = accountService.addBankAccount(accountCreationInformation);

        return ResponseEntity.ok(newBankAccount);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable("accountId") String accountId) {
        Account userAccount = accountService.getAccountInformationById(accountId);

        return ResponseEntity.ok(userAccount);
    }

    @PostMapping("/account/send-money")
    public ResponseEntity<?> sendMoney(@RequestParam("senderId") String senderId,
                                       @RequestParam("receiverId") String receiverId,
                                       @RequestParam("moneyToSend") int moneyToSend) {
        try {
            accountService.sendMoney(senderId, receiverId, moneyToSend);

            return ResponseEntity.ok().build();
        } catch (WrongAmountOfMoneyException | NotEnoughMoneyException | NoReceiverOrSenderAccountException |
                 MismatchOfCurrenciesException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/transfer-history/{accountId}")
    public ResponseEntity<?> getAllTransfersForAccount(@PathVariable("accountId") String accountId) {
        List<Transfer> transfers = accountService.getAllTransfersByAccount(accountId);

        return ResponseEntity.ok(transfers);
    }
}