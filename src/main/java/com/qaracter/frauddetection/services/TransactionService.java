package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionService {
    private List<Transaction> transactions;

    public TransactionService(){
        this.transactions = new ArrayList<>();
    }

    public boolean addTransaction(Transaction t){
        return this.transactions.add(t);
    }
}
