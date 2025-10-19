package com.personalbanking.personaltransaction.features.GetRecentTransferList.model;

public record GetRecentTransferListData (
        Long transfer_id,
    GetUser getUser,
    GetAccount getAccount
) {
}
