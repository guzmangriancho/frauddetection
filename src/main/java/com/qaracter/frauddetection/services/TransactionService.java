package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionService {
    private List<Transaction> transactions;

    public TransactionService(){
        this.transactions = new ArrayList<>();
    }

    public boolean addTransaction(Transaction t){
        return this.transactions.add(t);
    }

    public Transaction getTransactionById(Long id){
        return this.transactions.stream().filter(t -> t.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Transaction> getAlllTransactions(){
        return new ArrayList<>(this.transactions);
    }
}
