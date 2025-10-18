package com.personalbanking.personaltransaction.features.validateTransfer.repository.impl;

import com.personalbanking.personaltransaction.features.validateTransfer.dto.ValidateTransferDto;
import com.personalbanking.personaltransaction.features.validateTransfer.dto.AccountTypeDto;
import com.personalbanking.personaltransaction.features.validateTransfer.repository.AccountDetailRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountDetailsRepoImpl implements AccountDetailRepo {

    private final JdbcTemplate jdbcTemplate;

    public AccountDetailsRepoImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ValidateTransferDto> accountDetailResponseDtoRowMapper = (rs, rowNum) -> {

        AccountTypeDto accountType = new AccountTypeDto(
          rs.getString("name"),
          rs.getString("code")
        );

        return new ValidateTransferDto(
                        rs.getString("account_number"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getBigDecimal("current_balance"),
                        accountType
                );
    };

    @Override
    public Optional<ValidateTransferDto> findyByAccountNumber(String accountNumber) {

        String sql = """
                SELECT ad.account_number,ua.username,ua.email,ad.current_balance,at.name,at.code
                FROM account_detail ad
                JOIN user_account ua ON ad.user_id=ua.id
                JOIN account_type at ON ad.account_type_id=at.id
                WHERE ad.account_number=?
                """;

        return jdbcTemplate.query(sql,accountDetailResponseDtoRowMapper,accountNumber)
                .stream()
                .findFirst();
    }
}
