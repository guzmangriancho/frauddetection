package com.qaracter.frauddetection.controllers;

import com.qaracter.frauddetection.models.User;
import com.qaracter.frauddetection.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        return userService.deleteUser(id) ? "User deleted" : "User not found";
    }

    @PostMapping("/{userId}/accounts/{accountId}/deposit")
    public String deposit(@PathVariable Long userId, @PathVariable Long accountId, @RequestParam Double amount) {
        boolean success = userService.processDeposit(userId, accountId, amount);
        return success ? "Success" : "Account not found or Error";
    }

    @PostMapping("/{userId}/accounts/{accountId}/withdraw")
    public String withdraw(@PathVariable Long userId, @PathVariable Long accountId, @RequestParam Double amount) {
        boolean success = userService.processWithdrawal(userId, accountId, amount);
        return success ? "Success" : "Insufficient funds or Error";
    }
}