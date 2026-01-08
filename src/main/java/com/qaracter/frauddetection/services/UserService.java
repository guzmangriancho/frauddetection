package com.qaracter.frauddetection.services;

import com.qaracter.frauddetection.models.User;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> userList = new ArrayList<>();

    public User createUser(User user) {
        userList.add(user);
        return user;
    }

    public List<User> getAllUsers() {
        return userList;
    }

    public User getUserById(Long id) {
        return userList.stream()
                .filter(u -> u.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        if (user != null) {
            user.setName(userDetails.getName());
            return user;
        }
        return null;
    }

    public boolean deleteUser(Long id) {
        return userList.removeIf(u -> u.getId().equals(id));
    }

    public boolean processDeposit(Long userId, Long accountId, double amount) {
        User user = getUserById(userId);
        if (user != null) {
            return user.getAccounts().stream()
                    .filter(a -> a.getId().equals(accountId))
                    .findFirst()
                    .map(a -> {
                        a.deposit(amount);
                        return true;
                    }).orElse(false);
        }
        return false;
    }

    public boolean processWithdrawal(Long userId, Long accountId, double amount) {
        User user = getUserById(userId);
        if (user != null) {
            return user.getAccounts().stream()
                    .filter(a -> a.getId().equals(accountId))
                    .findFirst()
                    .map(a -> a.withdraw(amount))
                    .orElse(false);
        }
        return false;
    }
}
