package com.qaracter.frauddetection.controllers;

import com.qaracter.frauddetection.models.Account;
import com.qaracter.frauddetection.models.User;
import com.qaracter.frauddetection.services.AccountService;
import com.qaracter.frauddetection.services.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;

    public AccountController(AccountService accountService,
                             UserService userService) {
        this.accountService = accountService;
        this.userService = userService;
    }

    @PostMapping("/users/{userId}/accounts")
    public ResponseEntity<?> createAccount(@PathVariable Long userId,
                                           @RequestBody Account account) {

        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Account newAccount = new Account(
                account.getId(),
                userId,
                account.getBalance()
        );

        boolean created = accountService.addAccount(newAccount);
        if (!created) {
            return ResponseEntity.badRequest().body("Account already exists");
        }

        user.addAccount(newAccount);
        return ResponseEntity.ok(newAccount);
    }

    @GetMapping("/users/{userId}/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.getAccounts());
    }

    @GetMapping("/accounts/{accountId}")
    public ResponseEntity<Account> getAccount(@PathVariable Long accountId) {
        Account account = accountService.getAccount(accountId);
        return account != null
                ? ResponseEntity.ok(account)
                : ResponseEntity.notFound().build();
    }

    @PutMapping("/accounts/{accountId}")
    public ResponseEntity<?> updateAccount(@PathVariable Long accountId,
                                           @RequestBody Account updatedAccount) {

        boolean updated = accountService.updateAccount(accountId, updatedAccount);
        return updated
                ? ResponseEntity.ok(updatedAccount)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long accountId) {
        boolean deleted = accountService.deleteAccount(accountId);
        return deleted
                ? ResponseEntity.ok("Account deleted")
                : ResponseEntity.notFound().build();
    }
}
