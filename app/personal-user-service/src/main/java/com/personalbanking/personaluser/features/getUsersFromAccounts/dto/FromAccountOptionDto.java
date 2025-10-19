package com.personalbanking.personaluser.features.getUsersFromAccounts.dto;

public record FromAccountOptionDto(
        int id,
        String accountNumber,
        double balance
) {
}
