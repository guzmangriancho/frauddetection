package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.Account;
import com.qaracter.frauddetection.models.Transaction;

import java.util.ArrayList;
import java.util.List;

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

        checkFraud(transaction, senderId, recipientId, sender);

        transactions.add(transaction);
        return transaction;
    }

    private void checkFraud(Transaction transaction, Long senderId, Long recipientId, Account sender) {
        if (transaction.getAmount() > 5000) {
            transaction.setFlagged(true);
            System.out.println("Transaction with id " + transaction.getId() + " flagged as suspicious: > $5000");
        }

        long oneMinuteAgo = System.currentTimeMillis() - (60 * 1000);
        long count = transactions.stream()
                .filter(t -> t.getSenderAccountId().equals(senderId))
                .filter(t -> t.getTimestamp() > oneMinuteAgo)
                .count();
        if (count >= 5) {
            transaction.setFlagged(true);
            System.out.println("Transaction with id " + transaction.getId() + " flagged as suspicious: Transaction per minute limit reached");
        }

        long fiveMinutesAgo = System.currentTimeMillis() - (5 * 60 * 1000);
        Long currentUserId = sender.getUserId();

        List<Transaction> suspicious = transactions.stream()
                .filter(t -> t.getTimestamp() > fiveMinutesAgo)
                .filter(t -> t.getRecipientAccountId().equals(recipientId))
                .filter(t -> {
                    Account pastSender = accountService.getAccount(t.getSenderAccountId());
                    return pastSender.getUserId().equals(currentUserId);
                })
                .toList();

        if (!suspicious.isEmpty()) {
            transaction.setFlagged(true);
            for(Transaction t : suspicious ){
                t.setFlagged(true);
            }
            System.out.println("Transaction with id " + transaction.getId() + " flagged as suspicious: Pattern detected");
        }
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

    public Transaction setTransactionFlag(Long id, boolean flag) {
        Transaction transaction = getTransactionById(id);
        if (transaction != null) {
            transaction.setFlagged(flag);
            return transaction;
        }
        return null;
    }
}
