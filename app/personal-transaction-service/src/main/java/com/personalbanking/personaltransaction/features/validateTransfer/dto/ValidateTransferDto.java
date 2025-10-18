package com.personalbanking.personaltransaction.features.validateTransfer.dto;

import java.math.BigDecimal;

public record ValidateTransferDto(
        String accountNumber,
        String username,
        String email,
        BigDecimal currentBalance,
        AccountTypeDto accountType
) {
}
