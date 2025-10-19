package com.personalbanking.personaluser.features.getUsersFromAccounts.dto;

import java.util.List;

public record FromAccountDataDto(
        List<FromAccountOptionDto> fromAccountOptions
) {}
