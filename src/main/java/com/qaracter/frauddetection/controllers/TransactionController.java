package com.qaracter.frauddetection.controllers;


import com.qaracter.frauddetection.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping("/sender/{transferId}")
    public Long getSenderAccountId(Long transferId){
        return this.transactionService.getTransactionById(transferId).getSenderAccountId();
    }

    @GetMapping("/recipient/{transferId}")
    public Long getRecipientAccountId(Long transferId){
        return this.transactionService.getTransactionById(transferId).getRecipientAccountId();
    }

    @GetMapping("/amount/{transferId}")
    public double getAmount(Long transferId){
        return this.transactionService.getTransactionById(transferId).getAmount();
    }
}
