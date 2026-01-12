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

    @GetMapping("/lesser/{amount}")
    public List<Transaction> getTransactionsWithAmountLesserThan(double amount){
        return this.transactionService.getTransactionsWithAmountLesserThan(amount);
    }

    @GetMapping("/greater/{amount}")
    public List<Transaction> getTransactionsWithAmountGreaterThan(double amount){
        return this.transactionService.getTransactionsWithAmountGreaterThan(amount);
    }

    @GetMapping("/sender/{sender_account_id}/recipient/{recipient_account_id}")
    public List<Transaction> getTransactionsBetween(Long sender_account_id, Long recipient_acount_id){
        return this.transactionService.getTransactionsBetween(sender_account_id, recipient_acount_id);
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

    @GetMapping("/since/{time_in_seconds}")
    public List<Transaction> getTransactionsSince(long time_in_seconds){
        return this.transactionService.getTransactionsSince(time_in_seconds);
    }

    @GetMapping("/since/{time_in_seconds}/sender/{account_id}")
    public List<Transaction> getTransactionsSentSince(long time_in_seconds,  Long account_id){
        return this.transactionService.getTransactionsSentSince(time_in_seconds, account_id);
    }

    @GetMapping("/since/{time_in_seconds}/recipient/{account_id}")
    public List<Transaction> getTransactionsReceivedSince(long time_in_seconds, Long account_id){
        return this.transactionService.getTransactionsReceivedSince(time_in_seconds, account_id);
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
