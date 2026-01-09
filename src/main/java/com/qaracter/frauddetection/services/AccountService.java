package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.Account;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {

    private final Map<Long, Account> accounts;

    public AccountService() {
        this.accounts = new ConcurrentHashMap<>();
    }

    public boolean addAccount(Account account) {
        return accounts.putIfAbsent(account.getId(), account) == null;
    }

    public Account getAccount(Long accountId) {
        return accounts.get(accountId);
    }

    public Collection<Account> getAllAccounts() {
        return accounts.values();
    }

    public boolean updateAccount(Long accountId, Account updatedAccount) {
        if (!accounts.containsKey(accountId)) {
            return false;
        }
        accounts.put(accountId, updatedAccount);
        return true;
    }

    public boolean deleteAccount(Long accountId) {
        return accounts.remove(accountId) != null;
    }
}
