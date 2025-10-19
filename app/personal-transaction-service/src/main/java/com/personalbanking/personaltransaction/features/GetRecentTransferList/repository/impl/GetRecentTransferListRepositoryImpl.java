package com.personalbanking.personaltransaction.features.GetRecentTransferList.repository.impl;

import com.personalbanking.personaltransaction.features.GetRecentTransferList.model.GetAccount;
import com.personalbanking.personaltransaction.features.GetRecentTransferList.model.GetRecentTransferListData;
import com.personalbanking.personaltransaction.features.GetRecentTransferList.model.GetUser;
import com.personalbanking.personaltransaction.features.GetRecentTransferList.repository.GetRecentTransferListJdbcRepo;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class GetRecentTransferListRepositoryImpl implements GetRecentTransferListJdbcRepo {
    private JdbcTemplate jdbcTemplate;
    public GetRecentTransferListRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final RowMapper<GetRecentTransferListData> getRecentTransferListRowMapper=(rs,rowNum)->{
         GetUser getUser=null;
         GetAccount getAccount=null;

        Long userId=rs.getObject("user_id",Long.class);
        Long accountId=rs.getObject("account_id",Long.class);

        if(userId!=null) {
            getUser=new GetUser(
                    userId,
                    rs.getString("name")
            );
        }
        if(accountId!=null) {
            getAccount=new GetAccount(
                    accountId,
                    rs.getString("account_number")
            );
        }

        return new GetRecentTransferListData(
                rs.getLong("transfer_id"),
                getUser,
                getAccount
        );

    };

    @Override
    public List<GetRecentTransferListData> findAll() {
                String sql = """
                                SELECT
                                    t.id             AS transfer_id,
                                    u.id             AS user_id,
                                    u.name           AS name,
                                    a.id             AS account_id,
                                    a.account_number AS account_number
                                FROM transfers t
                                LEFT JOIN users u ON t.user_id = u.id
                                LEFT JOIN accounts a ON t.account_id = a.id
                                ORDER BY t.created_at DESC
                                LIMIT 5
                                """;
        return jdbcTemplate.query(sql,getRecentTransferListRowMapper);
    }
}
