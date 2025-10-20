package com.personalbanking.personaltransaction.features.confirmTransfer.dto;

import java.math.BigDecimal;

public record AccountDetailDto(
        Long id,
        String pin,
        BigDecimal currentBalance) {

}
