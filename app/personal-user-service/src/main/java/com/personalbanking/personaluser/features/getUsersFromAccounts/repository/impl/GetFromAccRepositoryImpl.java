package com.personalbanking.personaluser.features.getUsersFromAccounts.repository.impl;

import com.personalbanking.personaluser.features.getUsersFromAccounts.dto.FromAccountOptionDto;
import com.personalbanking.personaluser.features.getUsersFromAccounts.repository.GetFromAccRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class GetFromAccRepositoryImpl implements GetFromAccRepository {

    private final JdbcTemplate jdbcTemplate;

    public GetFromAccRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<FromAccountOptionDto> fromAccountOptionRowMapper = (rs, rowNum) -> {

        return new FromAccountOptionDto(
                rs.getInt("id"),
                rs.getString("account_number"),
                rs.getDouble("current_balance")
        );
    };

    @Override
    public List<FromAccountOptionDto> findAllFromAccountsByUserId(int userId) {

        String sql = "select id, account_number, current_balance from account_detail where user_id = ?";

        return jdbcTemplate.query(sql, fromAccountOptionRowMapper, userId);
    }
}
