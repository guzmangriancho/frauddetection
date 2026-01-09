package com.qaracter.frauddetection.models;

import java.util.ArrayList;
import java.util.List;

public class User {

    private Long id;
    private String name;
    private List<Account> accounts = new ArrayList<>();

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }
}
