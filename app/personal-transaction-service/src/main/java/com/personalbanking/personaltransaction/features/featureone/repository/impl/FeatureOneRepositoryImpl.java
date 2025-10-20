package com.personalbanking.personaltransaction.features.featureone.repository.impl;

import com.personalbanking.personaltransaction.features.featureone.repository.FeatureOneRepository;
import com.personalbanking.personaltransaction.features.nicknametransfer.models.AccountDetail;
import com.personalbanking.personaltransaction.features.nicknametransfer.models.Nickname;
import com.personalbanking.personaltransaction.features.nicknametransfer.models.Transaction;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.util.Optional;

public class FeatureOneRepositoryImpl implements FeatureOneRepository {

    private final JdbcTemplate jdbcTemplate;

    public FeatureOneRepositoryImpl(JdbcTemplate jdbcTemplate) {this.jdbcTemplate = jdbcTemplate;}


    private final RowMapper<Transaction> transactionRowMapper = (rs, rowNum) ->
            new Transaction(
                    rs.getLong("id"),
                    rs.getLong("creditAccountId"),
                    rs.getLong("debitAccountId"),
                    rs.getBigDecimal("amount"),
                    rs.getString("status"),
                    rs.getString("createdAt"),
                    rs.getString("updatedAt")
            );

    private final RowMapper<AccountDetail> accountDetailRowMapper = (rs, rowNum) ->
            new AccountDetail(
                    rs.getLong("id"),
                    rs.getLong("user_id"),
                    rs.getString("account_number"),
                    rs.getLong("account_type_id"),
                    rs.getLong("group_id"),
                    rs.getBigDecimal("current_balance"),
                    rs.getLong("role_id"),
                    rs.getLong("nickname_id"),
                    rs.getLong("kyc_id"),
                    rs.getString("created_at"),
                    rs.getString("updated_at")
            );

    @Override
    public Optional<AccountDetail> findAccountByUserId(Long userId) {
        String sql = "SELECT * FROM account_detail WHERE user_id = ?";
        return jdbcTemplate.query(sql,accountDetailRowMapper, userId)
                .stream()
                .findFirst();
    }

    @Override
    public String prepareTransfer(Long fromAccountId, Long toAccountId, Long userId) {
        return "TXN_" + java.util.UUID.randomUUID();
    }

    @Override
    public Long saveTransaction(Transaction transaction) {
        String sql = """
                INSERT INTO transactions (
                    credit_account_id,
                    debit_account_id,
                    amount,
                    status,
                    created_at,
                    updated_at)
                VALUES (?, ?, ?, ?, ?, ?)
                RETURNING id
                """;
        return jdbcTemplate.queryForObject(
                sql,
                Long.class,
                transaction.creditAccountId(),
                transaction.debitAccountId(),
                transaction.amount(),
                transaction.status(),
                transaction.createdAt(),
                transaction.updatedAt()
        );
    }
}
