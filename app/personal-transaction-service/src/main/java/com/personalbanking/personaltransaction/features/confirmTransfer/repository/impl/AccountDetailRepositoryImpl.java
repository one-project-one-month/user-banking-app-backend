package com.personalbanking.personaltransaction.features.confirmTransfer.repository.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import com.personalbanking.personaltransaction.features.confirmTransfer.dto.AccountDetailDto;
import com.personalbanking.personaltransaction.features.confirmTransfer.repository.AccountDetailRepository;

@Repository
public class AccountDetailRepositoryImpl implements AccountDetailRepository {

    private final JdbcTemplate jdbcTemplate;

    public AccountDetailRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<AccountDetailDto> rowMapper = new RowMapper<>() {
        @Override
        public AccountDetailDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new AccountDetailDto(
                    rs.getLong("id"),
                    rs.getString("pin"),
                    rs.getBigDecimal("current_balance"));
        }
    };

    @Override
    public Optional<AccountDetailDto> findById(Long id) {
        String sql = """
                    SELECT id, pin, current_balance
                    FROM account_detail
                    WHERE id = ?
                """;

        try {
            AccountDetailDto dto = jdbcTemplate.queryForObject(sql, rowMapper, id);
            return Optional.ofNullable(dto);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public void updateBalance(Long id, BigDecimal newBalance) {
        String sql = "UPDATE account_detail SET current_balance = ? WHERE id = ?";

        jdbcTemplate.update(sql, newBalance, id);
    }

}
