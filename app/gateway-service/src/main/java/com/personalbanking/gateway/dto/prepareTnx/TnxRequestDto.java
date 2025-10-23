package com.personalbanking.gateway.dto.prepareTnx;

public record TnxRequestDto (
        long fromAccountId,
        long toAccountId
){

}
