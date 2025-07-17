package com.demo.demo.repository.Transaction;

import com.demo.demo.entity.Transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}
