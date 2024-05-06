package com.example.financetracker.controller;

import com.example.financetracker.models.Transaction;
import com.example.financetracker.models.TransactionCategory;
import com.example.financetracker.models.User;
import com.example.financetracker.service.TrackerService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
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
    public List<Transaction> getSummaryOperations() {
        return trackerService.getSummaryOperations();
    }


    @PostMapping("/transactions")
    public ResponseEntity<Void> createTransaction(@RequestParam("description") String description,
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
            return ResponseEntity.ok().build();
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

    @PostMapping("/users")
    public ResponseEntity<Void> createUser(@RequestParam("username") String username,
                                           @RequestParam("email") String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        trackerService.createUser(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/bulk")
    public ResponseEntity<Void> createUsers(@RequestBody List<User> userRequests) {
        List<User> users = userRequests.stream()
                .map(userRequest -> {
                    User user = new User();
                    user.setUsername(userRequest.getUsername());
                    user.setEmail(userRequest.getEmail());
                    return user;
                })
                .collect(Collectors.toList());

        trackerService.createUsers(users);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/addTransaction")
    public String showTransactionForm(Model model) {
        model.addAttribute("categories", trackerService.getAllCategories());
        model.addAttribute("users", trackerService.getAllUsers());
        return "transaction_form";
    }

    @PostMapping("/addTransaction")
    public String addTransaction(@RequestParam("description") String description,
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

        return "redirect:/addTransaction"; // Перенаправляем на главную страницу после добавления транзакции
    }

    @GetMapping("/addUser")
    public String showUserForm() {
        return "user_form";
    }

    @PostMapping("/addUser")
    public String addUser(@RequestParam("username") String username,
                          @RequestParam("email") String email) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        trackerService.createUser(user);
        return "redirect:/addUser"; // Перенаправляем на главную страницу после добавления пользователя
    }

    @DeleteMapping("/deleteTransaction/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id) {
        trackerService.deleteTransaction(id);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        trackerService.deleteUser(id);
        return "redirect:/deleteUser"; // Перенаправляем на главную страницу после удаления пользователя
    }


    @GetMapping("/transactions")
    public String showAllTransactions(Model model) {
        List<Transaction> transactions = trackerService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transactions"; // Файл transactions.html должен быть создан
    }

    @GetMapping("/deleteTransaction")
    public String showDeleteTransactionForm(Model model) {
        List<Transaction> transactions = trackerService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "delete_transaction_form";
    }

    @GetMapping("/deleteUser")
    public String showDeleteUserForm(Model model) {
        List<User> users = trackerService.getAllUsers();
        model.addAttribute("users", users);
        return "delete_user_form";
    }

    @GetMapping("/updateTransaction/{id}")
    public String showUpdateTransactionForm(@PathVariable("id") Long id, Model model) {
        Transaction transaction = trackerService.getTransactionById(id);
        if (transaction == null) {
            throw new EntityNotFoundException("Transaction with ID " + id + " not found");
        }
        model.addAttribute("transaction", transaction);
        model.addAttribute("categories", trackerService.getAllCategories());
        model.addAttribute("users", trackerService.getAllUsers());
        return "update_transaction_form";
    }

    @PostMapping("/updateTransaction")
    public String updateTransaction(@RequestParam("id") Long id,
                                    @RequestParam("description") String description,
                                    @RequestParam("amount") double amount,
                                    @RequestParam("username") String username,
                                    @RequestParam("category") String categoryName,
                                    Model model) {
        Transaction existingTransaction = trackerService.getTransactionById(id);
        if (existingTransaction == null) {
            throw new EntityNotFoundException("Transaction with ID " + id + " not found");
        }

        if (description != null && !description.isEmpty()) {
            existingTransaction.setDescription(description);
        }

        if (amount != 0) {
            existingTransaction.setAmount(amount);
        }

        User user = trackerService.getUserByUsername(username);
        if (user != null) {
            existingTransaction.setUser(user);
        } else {
        }
        TransactionCategory category = trackerService.getCategoryByName(categoryName);
        if (category != null) {
            existingTransaction.setCategory(category);
        } else {
        }
        trackerService.updateTransaction(existingTransaction);
        model.addAttribute("transaction", existingTransaction);
        return "redirect:/transactions";
    }


}
