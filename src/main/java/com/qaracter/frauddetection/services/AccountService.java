package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.Account;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {

    private final Map<Long, Account> accounts = new ConcurrentHashMap<>();

    public boolean addAccount(Account account) {
        return accounts.putIfAbsent(account.getId(), account) == null;
    }

    public Account getAccount(Long accountId) {
        return accounts.get(accountId);
    }
}
