package com.personalbanking.personaltransaction.features.confirmTransfer.repository;

import java.math.BigDecimal;
import java.util.Optional;
import com.personalbanking.personaltransaction.features.confirmTransfer.dto.AccountDetailDto;

public interface AccountDetailRepository {
    Optional<AccountDetailDto> findById(Long id);

    void updateBalance(Long id, BigDecimal newBalance);
}
