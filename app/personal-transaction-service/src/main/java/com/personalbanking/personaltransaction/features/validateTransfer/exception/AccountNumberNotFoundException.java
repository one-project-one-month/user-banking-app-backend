package com.personalbanking.personaltransaction.features.validateTransfer.exception;

public class AccountNumberNotFoundException extends RuntimeException{

    public AccountNumberNotFoundException(String message){
        super(message);
    }
}
