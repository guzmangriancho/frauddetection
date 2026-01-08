package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.Account;

import java.util.HashMap;
import java.util.Map;

public class AccountService {

    private final Map<Long, Account> accounts = new HashMap<Long, Account>();

    public boolean addAccount(Account account) {
        return accounts.putIfAbsent(account.getId(), account) == null;
    }

    public Account getAccount(Long accountId) {
        return accounts.get(accountId);
    }
}
