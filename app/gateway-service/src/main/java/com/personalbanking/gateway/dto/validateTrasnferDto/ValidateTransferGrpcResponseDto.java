package com.personalbanking.gateway.dto.validateTrasnferDto;

public record ValidateTransferGrpcResponseDto(
        String accountNumber,
        String email,
        String userName,
        Long currentBalance,
        AccountTypeGrpcResponseDto accountType
) {
}
