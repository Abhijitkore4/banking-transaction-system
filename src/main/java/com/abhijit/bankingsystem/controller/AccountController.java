package com.abhijit.bankingsystem.controller;

import com.abhijit.bankingsystem.entity.Account;
import com.abhijit.bankingsystem.service.AccountService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{userId}")
    public Account createAccount(@PathVariable Long userId) {
        return accountService.createAccount(userId);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @GetMapping("/{id}/balance")
    public Object getBalance(@PathVariable Long id) {
        return accountService.getAccountById(id).getBalance();
    }
}