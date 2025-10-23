package com.personalbanking.gateway.dto.prepareTnx;

public record TnxResponseDto(
        boolean success,
        String message,
        String transactionId,
        String status
) {

}
