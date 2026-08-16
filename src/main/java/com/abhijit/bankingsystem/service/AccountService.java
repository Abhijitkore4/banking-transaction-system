package com.abhijit.bankingsystem.service;

import com.abhijit.bankingsystem.entity.Account;
import com.abhijit.bankingsystem.entity.User;
import com.abhijit.bankingsystem.repository.AccountRepository;
import com.abhijit.bankingsystem.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository,
                          UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public Account createAccount(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();

        account.setAccountNumber(generateAccountNumber());
        account.setBalance(BigDecimal.ZERO);
        account.setAccountType("SAVINGS");
        account.setStatus("ACTIVE");
        account.setUser(user);

        return accountRepository.save(account);
    }

    public Account getAccountById(Long id) {

        return accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    private String generateAccountNumber() {
        return "AC" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 10)
                .toUpperCase();
    }

    public Account deposit(Long accountId, BigDecimal amount) {

        Account account = getAccountById(accountId);

        validateAccount(account);
        validateAmount(amount);

        account.setBalance(account.getBalance().add(amount));

        return accountRepository.save(account);
    }

    public Account withdraw(Long accountId, BigDecimal amount) {

        Account account = getAccountById(accountId);

        validateAccount(account);
        validateAmount(amount);

        if (account.getBalance().compareTo(amount) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        account.setBalance(account.getBalance().subtract(amount));

        return accountRepository.save(account);
    }

    private void validateAmount(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero");
        }
    }

    private void validateAccount(Account account) {

        if (!"ACTIVE".equals(account.getStatus())) {
            throw new RuntimeException("Account is not active");
        }
    }
}