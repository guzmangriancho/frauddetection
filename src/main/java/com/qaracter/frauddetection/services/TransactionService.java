package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.Account;
import com.qaracter.frauddetection.models.Transaction;
import com.qaracter.frauddetection.services.AccountService;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionService {
    private List<Transaction> transactions;
    private final AccountService accountService;

    public TransactionService(){
        this.transactions = new ArrayList<>();
        this.accountService = new AccountService();
    }

    public Transaction transfer(Long senderId, Long recipientId, double amount) {

        Account sender = accountService.getAccount(senderId);
        Account recipient = accountService.getAccount(recipientId);

        if (!sender.withdraw(amount)) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        recipient.deposit(amount);

        Transaction transaction = new Transaction(
                System.currentTimeMillis(),
                senderId,
                recipientId,
                amount
        );

        transactions.add(transaction);
        return transaction;
    }

    public List<Transaction> getTransactionsByAccount(Long accountId) {
        List<Transaction> result = new ArrayList<>();

        result.addAll(getAllTransactionsFromAccount(accountId));
        result.addAll(getAllTransactionsToRecipient(accountId));

        return result;
    }

    public boolean addTransaction(Transaction t){
        return this.transactions.add(t);
    }

    public Transaction getTransactionById(Long id){
        return this.transactions.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Transaction> getAllTransactions(){
        return new ArrayList<>(this.transactions);
    }

    public List<Transaction> getFlaggedTransactions(){
        return this.transactions.stream().filter(Transaction::isFlagged).toList();
    }

    public List<Transaction> getAllTransactionsFromAccount(Long accountId){
        return this.transactions.stream().filter(t -> t.getSenderAccountId().equals(accountId)).toList();
    }

    public List<Transaction> getTransactionsSince(long timeSince){
        return this.transactions.stream().filter(t -> t.getTimestamp() > timeSince).toList();
    }

    public List<Transaction> getTransactionsSinceAccount(long timeSince, Long accountId){
        return this.getTransactionsSince(timeSince).stream().filter(t -> t.getSenderAccountId().equals(accountId)).toList();
    }

    public List<Transaction> getTransactionsWithAmountGreaterThan(double amount){
        return this.transactions.stream().filter(t -> t.getAmount() > amount).toList();
    }

    public List<Transaction> getTransactionsWithAmountLesserThan(double amount){
        return this.transactions.stream().filter(t -> t.getAmount() < amount).toList();
    }

    public List<Transaction> getAllTransactionsToRecipient(Long recipientAccountId) {
        return this.transactions.stream()
                .filter(t -> t.getRecipientAccountId().equals(recipientAccountId))
                .toList();
    }

    public List<Transaction> getTransactionsReceivedSince(long timeSince, Long recipientAccountId) {
        return this.getTransactionsSince(timeSince).stream()
                .filter(t -> t.getRecipientAccountId().equals(recipientAccountId))
                .toList();
    }

    public List<Transaction> getTransactionsBetween(Long senderAccountId, Long recipientAccountId) {
        return this.transactions.stream()
                .filter(t -> t.getSenderAccountId().equals(senderAccountId) &&
                        t.getRecipientAccountId().equals(recipientAccountId))
                .toList();
    }
}
