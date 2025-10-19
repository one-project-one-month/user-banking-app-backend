package com.personalbanking.gateway.dto.validateTrasnferDto;

import java.util.List;
import java.util.Map;

public record ValidateTransferResponseDto<T>(
        int code,
        String message,
        List<Map<String,T>> date
) {
}
