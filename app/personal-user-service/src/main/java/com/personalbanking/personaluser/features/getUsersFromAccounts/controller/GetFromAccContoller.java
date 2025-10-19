package com.personalbanking.personaluser.features.getUsersFromAccounts.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/personal-banking/users")
public class GetFromAccContoller {

    @GetMapping("/from-accounts")
    public void getUsersFromAccounts() {

    }
}
