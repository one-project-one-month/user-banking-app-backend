package com.personalbanking.gateway.dto.confirmTransferDto;

public record ConfirmTransferRequestDto(
        Long fromAccountId,
        Long toAccountId,
        double amount,
        String note,
        String pin) {

}
