package com.personalbanking.personaltransaction.features.GetRecentTransferList.model;

public record GetRecentTransferListData (
    GetUser getUser,
    GetAccount getAccount
) {
}
