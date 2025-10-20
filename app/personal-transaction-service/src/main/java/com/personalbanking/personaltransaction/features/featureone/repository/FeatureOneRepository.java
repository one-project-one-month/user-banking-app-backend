package com.personalbanking.personaltransaction.features.featureone.repository;

import com.personalbanking.personaltransaction.features.nicknametransfer.models.AccountDetail;
import com.personalbanking.personaltransaction.features.nicknametransfer.models.Transaction;

import java.util.Optional;

public interface FeatureOneRepository {
    Optional<AccountDetail> findAccountByUserId(Long userId);
    String prepareTransfer(Long fromAccountId, Long toAccountId, Long userId);
    Long saveTransaction(Transaction transaction);



}