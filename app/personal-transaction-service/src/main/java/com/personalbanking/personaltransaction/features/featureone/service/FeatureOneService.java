package com.personalbanking.personaltransaction.features.featureone.service;

import com.personalbanking.personaltransaction.features.nicknametransfer.models.AccountDetail;

public interface FeatureOneService {

    AccountDetail createTransaction(Long accountId);
}
