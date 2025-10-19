package com.personalbanking.gateway.dto.getUserFromAccounts;

public record FromAccountOptionDto(
        long id,
        String accountNumber,
        double balance
) {
}
