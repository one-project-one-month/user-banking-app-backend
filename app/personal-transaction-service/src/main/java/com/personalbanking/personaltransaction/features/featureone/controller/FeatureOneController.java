package com.personalbanking.personaltransaction.features.featureone.controller;

import com.personalbanking.personaltransaction.features.featureone.service.FeatureOneService;
import com.personalbanking.personaltransaction.features.nicknametransfer.models.AccountDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/personal-banking/users/prepare-transaction")

public class FeatureOneController {
    private final FeatureOneService featureOneService;

    @Autowired
    public FeatureOneController(FeatureOneService featureOneService) {
        this.featureOneService = featureOneService;
    }


    @PostMapping("{accountId}")
    public ResponseEntity<AccountDetail> createTransaction(@PathVariable Long accountId){
        AccountDetail acc = featureOneService.createTransaction(accountId);
        return ResponseEntity.ok(acc);
    }
}
