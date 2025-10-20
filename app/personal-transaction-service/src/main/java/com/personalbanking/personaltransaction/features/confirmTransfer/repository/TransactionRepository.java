package com.personalbanking.personaltransaction.features.confirmTransfer.repository;

import java.math.BigDecimal;

public interface TransactionRepository {

    void insertTransaction(
            Long creditAccountId,
            Long debitAccountId,
            BigDecimal amount,
            String status,
            String note);

}
