package com.example.finance_tracker.controller;
import com.example.finance_tracker.models.Transaction;
import com.example.finance_tracker.service.TrackerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrackerController {

    private final TrackerService trackerService;

    public TrackerController(TrackerService trackerService) {
        this.trackerService = trackerService;
    }

    @GetMapping(value="")
    public String hello() {
        return "Hello world";
    }

    @GetMapping("/summary")
    public String getSummaryOperations() {
        return trackerService.getSummaryOperations();
    }

    @PostMapping("/transactions")
    public void createTransaction(@RequestParam("description") String description,
                                  @RequestParam("amount") double amount) {
        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);
        trackerService.createTransaction(transaction);
    }

    @GetMapping("/transactions/{id}")
    public Transaction getTransactionById(@PathVariable Long id) {
        return trackerService.getTransactionById(id);
    }

    @PutMapping("/transactions/{id}")
    public void updateTransaction(@RequestBody Transaction transaction) {
        trackerService.updateTransaction(transaction);
    }

    @DeleteMapping("/transactions/{id}")
    public void deleteTransaction(@PathVariable Long id) {
        trackerService.deleteTransaction(id);
    }

}


