package com.personalbanking.personaluser.features.getUsersFromAccounts.repository;

import com.personalbanking.personaluser.features.getUsersFromAccounts.dto.FromAccountOptionDto;
import java.util.List;

public interface GetFromAccRepository {
    List<FromAccountOptionDto> findAllFromAccountsByUserId(int userId);
}
