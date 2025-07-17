package com.demo.demo.api;

import com.demo.demo.entity.Transaction.Transaction;
import com.demo.demo.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin(origins = "*")
public class TransactionAPI {

    @Autowired
    TransactionService transactionService;

    @GetMapping
    public ResponseEntity<?> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @PostMapping
    public ResponseEntity<?> addTransaction(@RequestBody Transaction transaction) {
        transaction.setCreatedAt(java.time.LocalDateTime.now());
        Transaction savedTransaction = transactionService.saveTransaction(transaction);
        return ResponseEntity.ok(savedTransaction);
    }
}
