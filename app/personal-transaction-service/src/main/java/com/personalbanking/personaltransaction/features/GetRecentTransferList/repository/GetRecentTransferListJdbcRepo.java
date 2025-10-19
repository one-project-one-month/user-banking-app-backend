package com.personalbanking.personaltransaction.features.GetRecentTransferList.repository;

import com.personalbanking.personaltransaction.features.GetRecentTransferList.model.GetRecentTransferListData;

import java.util.List;


public interface GetRecentTransferListJdbcRepo {
    List<GetRecentTransferListData> findAll();
}
