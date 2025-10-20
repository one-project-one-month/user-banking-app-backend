package com.personalbanking.personaltransaction.features.confirmTransfer.repository.impl;

import java.math.BigDecimal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.personalbanking.personaltransaction.features.confirmTransfer.repository.TransactionRepository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final JdbcTemplate jdbcTemplate;

    public TransactionRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void insertTransaction(Long creditAccountId, Long debitAccountId, BigDecimal amount,
            String status, String note) {
        String sql =
                """
                            INSERT INTO transactions (credit_account_id, debit_account_id, amount, status, note)
                            VALUES (?, ?, ?, ?, ?)
                        """;

        jdbcTemplate.update(sql, creditAccountId, debitAccountId, amount, status, note);
    }

}
