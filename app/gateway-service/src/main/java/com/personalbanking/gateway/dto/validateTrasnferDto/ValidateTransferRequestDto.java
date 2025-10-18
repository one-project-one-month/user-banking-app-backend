package com.personalbanking.gateway.dto.validateTrasnferDto;

public record ValidateTransferRequestDto(
        String fromAccountId,
        String toAccountId
) {
}
