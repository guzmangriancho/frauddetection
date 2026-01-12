package com.qaracter.frauddetection.controllers;


import com.qaracter.frauddetection.models.Transaction;
import com.qaracter.frauddetection.models.TransferRequest;
import com.qaracter.frauddetection.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody TransferRequest request) {
        return transactionService.transfer(
                request.getSenderAccountId(),
                request.getRecipientAccountId(),
                request.getAmount()
        );
    }

    @GetMapping()
    public List<Transaction> getAllTransactions(){
        return this.transactionService.getAllTransactions();
    }

    @GetMapping("/flagged")
    public List<Transaction> getFlaggedTransactions(){
        return this.transactionService.getFlaggedTransactions();
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

    @PatchMapping("/{id}/flag")
    public Transaction flagTransaction( @PathVariable Long id, boolean flag) {
        Transaction transaction = transactionService.setTransactionFlag(id, flag);
        if (transaction == null) {
            throw new RuntimeException("Transaction not found");
        }
        return transaction;
    }
}
