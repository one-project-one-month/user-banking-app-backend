package com.personalbanking.gateway.dto.getUserFromAccounts;

import java.util.List;

public record FromAccountDataDto(
        List<FromAccountOptionDto> fromAccountOptions
) {}
