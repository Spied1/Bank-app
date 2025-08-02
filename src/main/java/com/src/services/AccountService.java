package com.src.services;

import com.src.components.UserDetailsImpl;
import com.src.exeptions.account.MismatchOfCurrenciesException;
import com.src.exeptions.account.NoReceiverOrSenderAccountException;
import com.src.exeptions.account.NotEnoughMoneyException;
import com.src.exeptions.account.WrongAmountOfMoneyException;
import com.src.models.Account;
import com.src.models.DTO.AccountCreationInformation;
import com.src.models.Transfer;
import com.src.models.User;
import com.src.repositorys.AccountRepository;
import com.src.repositorys.TransferRepository;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {
    private final AccountRepository bankAccountRepository;

    private final UserService userService;

    private final TransferRepository transferRepository;

    public AccountService(AccountRepository bankAccountRepository, UserService userService, TransferRepository transferRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
        this.transferRepository = transferRepository;
    }

    public Account getAccountInformationById(String accountId) {
        Optional<Account> account = bankAccountRepository.findById(accountId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (account.isEmpty()) {
            throw new IllegalArgumentException();
        }

        if (!Objects.equals(account.get().getUser().getId(), getUserId(authentication))) {
            throw new IllegalArgumentException();
        }

        return account.get();
    }

    public List<Account> getAllBankAccountsForUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return userService.getUser(authentication).getAccounts();
    }

    public Account addBankAccount(AccountCreationInformation accountCreationInformation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        User user = userService.getUser(authentication);
        Account account = new Account();
        account.setUser(user);
        user.getAccounts().add(account);

        account.setName(accountCreationInformation.getName());
        account.setCurrency(accountCreationInformation.getCurrency());
        Date date = new Date();

        account.setCreatedAt(date);
        account.setUpdatedAt(date);
        bankAccountRepository.save(account);
        userService.updateUser(user);

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
    public void sendMoney(String senderAccountId,
                          String receiverAccountId, int moneyToSend) throws NotEnoughMoneyException, NoReceiverOrSenderAccountException, WrongAmountOfMoneyException, MismatchOfCurrenciesException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (moneyToSend <= 0) {
            throw new WrongAmountOfMoneyException();
        }

        Optional<Account> senderAccount = bankAccountRepository.findById(senderAccountId);
        Optional<Account> receiverAccount = bankAccountRepository.findById(receiverAccountId);

        if (senderAccount.isEmpty() || receiverAccount.isEmpty()) {
            throw new NoReceiverOrSenderAccountException();
        }

        if (!Objects.equals(getUserId(authentication), senderAccount.get().getUser().getId())) {
            throw new IllegalArgumentException();
        }

        if (senderAccount.get().getBalance() <= moneyToSend) {
            throw new NotEnoughMoneyException();
        }

        if (senderAccount.get().getCurrency() != receiverAccount.get().getCurrency()) {
            throw new MismatchOfCurrenciesException();
        }

        senderAccount.get().setBalance(senderAccount.get().getBalance() - moneyToSend);
        receiverAccount.get().setBalance(receiverAccount.get().getBalance() + moneyToSend);
        Transfer newTransfer = getTransfer(moneyToSend, senderAccount, receiverAccount);

        senderAccount.get().setUpdatedAt(new Date());
        receiverAccount.get().setUpdatedAt(new Date());

        transferRepository.save(newTransfer);
        bankAccountRepository.save(senderAccount.get());
        bankAccountRepository.save(receiverAccount.get());
    }

    private Transfer getTransfer(int moneyToSend, Optional<Account> senderAccount, Optional<Account> receiverAccount) throws NoReceiverOrSenderAccountException {
        Transfer newTransfer = new Transfer();

        if (senderAccount.isEmpty() || receiverAccount.isEmpty()) {
            throw new NoReceiverOrSenderAccountException();
        }

        newTransfer.setSender(senderAccount.get().getUser());
        newTransfer.setSenderAccount(senderAccount.get());

        newTransfer.setReceiver(receiverAccount.get().getUser());
        newTransfer.setReceiverAccount(receiverAccount.get());

        newTransfer.setAmountOfMoney(moneyToSend);
        newTransfer.setCurrency(senderAccount.get().getCurrency());

        newTransfer.setTimeOfTransfer(new Date());
        return newTransfer;
    }

    public List<Transfer> getAllTransfersByAccount(String accountId) {
        List<Transfer> transfers = transferRepository.getAllTransfersByAccountId(accountId);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (transfers.isEmpty()) {
            return null;
        }

        if (!Objects.equals(getUserId(authentication), transfers.getFirst().getSender().getId())) {
            throw new IllegalArgumentException();
        }

        return transfers;
    }
}