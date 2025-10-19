package com.personalbanking.gateway.dto.getrecenttransferlist;


public record GetRecentTransferListDto(
    Long id,
    UserDto user,
    AccountDto account

) {
    
}
