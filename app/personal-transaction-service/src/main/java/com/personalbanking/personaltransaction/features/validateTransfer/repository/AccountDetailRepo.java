package com.personalbanking.personaltransaction.features.validateTransfer.repository;

import com.personalbanking.personaltransaction.features.validateTransfer.dto.ValidateTransferDto;

import java.util.Optional;

public interface AccountDetailRepo {
    public Optional<ValidateTransferDto> findyByAccountNumber(String accountNumber);
}
