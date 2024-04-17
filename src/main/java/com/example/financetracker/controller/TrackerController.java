package com.example.financetracker.controller;
import com.example.financetracker.models.Transaction;
import com.example.financetracker.models.TransactionCategory;
import com.example.financetracker.models.User;
import com.example.financetracker.service.TrackerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class TrackerController {

    private final TrackerService trackerService;

    public TrackerController(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @GetMapping(value = "")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/summary")
    public String getSummaryOperations() {
        return trackerService.getSummaryOperations();
    }

    @PostMapping("/transactions")
    public void createTransaction(@RequestParam("description") String description,
                                  @RequestParam("amount") double amount,
                                  @RequestParam("username") String username,
                                  @RequestParam("category") String categoryName) {
        User user = trackerService.getUserByUsername(username);

        if (user != null) {
            Transaction transaction = new Transaction();
            transaction.setDescription(description);
            transaction.setAmount(amount);
            transaction.setUser(user);

            TransactionCategory category = trackerService.getCategoryByName(categoryName);
            if (category == null) {
                category = new TransactionCategory();
                category.setName(categoryName);
                trackerService.createCategory(category);
            }
            transaction.setCategory(category);

            trackerService.createTransaction(transaction);
        } else {
            throw new EntityNotFoundException("User with username " + username + " not found");
        }
    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return trackerService.getTransactionById(id);
    }

    @GetMapping("/transactions/user/{username}")
    public List<Transaction> getTransactionsByUser(@PathVariable String username) {
        return trackerService.findTransactionsByUserUsername(username);
    }

    @PutMapping("/transactions/{id}")
    public void updateTransaction(@PathVariable Long id, @RequestBody Transaction updatedTransaction) {
        Transaction existingTransaction = trackerService.getTransactionById(id);
        if (existingTransaction == null) {
            throw new EntityNotFoundException("Transaction with ID " + id + " not found");
        }

        if (updatedTransaction.getDescription() != null) {
            existingTransaction.setDescription(updatedTransaction.getDescription());
        }

        if (updatedTransaction.getAmount() != 0) {
            existingTransaction.setAmount(updatedTransaction.getAmount());
        }

        if (updatedTransaction.getUser() != null) {
            existingTransaction.setUser(updatedTransaction.getUser());
        }

        if (updatedTransaction.getCategory() != null) {
            existingTransaction.setCategory(updatedTransaction.getCategory());
        }

        trackerService.updateTransaction(existingTransaction);
    }

    @DeleteMapping("/transactions/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        trackerService.deleteTransaction(id);
    }

    @PostMapping("/users")
    public void createUser(@RequestParam("username") String username,
                           @RequestParam("email") String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        trackerService.createUser(user);
    }

    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable Long id) {
        trackerService.deleteUser(id); }
    }