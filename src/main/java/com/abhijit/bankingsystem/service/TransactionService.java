package com.abhijit.bankingsystem.service;

import com.abhijit.bankingsystem.dto.TransferRequest;
import com.abhijit.bankingsystem.entity.Account;
import com.abhijit.bankingsystem.entity.Transaction;
import com.abhijit.bankingsystem.repository.AccountRepository;
import com.abhijit.bankingsystem.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionService(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction transfer(TransferRequest request) {

        validateRequest(request);

        Account fromAccount = accountRepository.findById(
                request.getFromAccountId()
        ).orElseThrow(() ->
                new RuntimeException("Sender account not found")
        );

        Account toAccount = accountRepository.findById(
                request.getToAccountId()
        ).orElseThrow(() ->
                new RuntimeException("Receiver account not found")
        );

        validateAccounts(fromAccount, toAccount);

        if (fromAccount.getBalance()
                .compareTo(request.getAmount()) < 0) {

            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(
                fromAccount.getBalance()
                        .subtract(request.getAmount())
        );

        toAccount.setBalance(
                toAccount.getBalance()
                        .add(request.getAmount())
        );

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();

        transaction.setTransactionReference(
                generateTransactionReference()
        );

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setAmount(request.getAmount());
        transaction.setType("TRANSFER");
        transaction.setStatus("SUCCESS");
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);
    }

    private void validateRequest(TransferRequest request) {

        if (request.getFromAccountId() == null ||
                request.getToAccountId() == null) {

            throw new RuntimeException("Account IDs are required");
        }

        if (request.getFromAccountId()
                .equals(request.getToAccountId())) {

            throw new RuntimeException(
                    "Cannot transfer money to the same account"
            );
        }

        if (request.getAmount() == null ||
                request.getAmount()
                        .compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException(
                    "Transfer amount must be greater than zero"
            );
        }
    }

    private void validateAccounts(
            Account fromAccount,
            Account toAccount) {

        if (!"ACTIVE".equals(fromAccount.getStatus())) {
            throw new RuntimeException("Sender account is not active");
        }

        if (!"ACTIVE".equals(toAccount.getStatus())) {
            throw new RuntimeException("Receiver account is not active");
        }
    }

    private String generateTransactionReference() {

        return "TXN-" +
                UUID.randomUUID()
                        .toString()
                        .replace("-", "")
                        .substring(0, 10)
                        .toUpperCase();
    }
}