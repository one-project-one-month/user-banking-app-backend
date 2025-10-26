package com.personalbanking.gateway.dto.getrecenttransferlist;


public record GetRecentTransferListDto(
        long id, UserDto user,
        AccountDto account

) {
    
}
