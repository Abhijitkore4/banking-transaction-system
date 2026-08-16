package com.abhijit.bankingsystem.controller;

import com.abhijit.bankingsystem.dto.TransferRequest;
import com.abhijit.bankingsystem.entity.Transaction;
import com.abhijit.bankingsystem.service.TransactionService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(
            TransactionService transactionService) {

        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public Transaction transfer(
            @RequestBody TransferRequest request) {

        return transactionService.transfer(request);
    }
}