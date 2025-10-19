package com.personalbanking.gateway.controller.getrecenttransfer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.personalbanking.GetRecentTransferList.GetRecentTransferListGrpc;
import com.personalbanking.GetRecentTransferList.GetRecentTransferListRequest;
import com.personalbanking.GetRecentTransferList.GetRecentTransferListResponse;
import com.personalbanking.gateway.dto.getrecenttransferlist.AccountDto;
import com.personalbanking.gateway.dto.getrecenttransferlist.GetRecentTransferListDto;
import com.personalbanking.gateway.dto.getrecenttransferlist.UserDto;

import net.devh.boot.grpc.client.inject.GrpcClient;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/v1")
public class GetRecentTransferListController {
    @GrpcClient("personal-transaction-service")
    private GetRecentTransferListGrpc.GetRecentTransferListBlockingStub getRecentTransferListStub;

    @GetMapping("/recent-transfers")
    public ResponseEntity<List<GetRecentTransferListDto>> getRecentTransferList(){
        GetRecentTransferListRequest request = GetRecentTransferListRequest.newBuilder().build();
        GetRecentTransferListResponse response = getRecentTransferListStub.getRecentTransferList(request);

        List<GetRecentTransferListDto> recentTransfers = response.getGetUserList().stream()
            .map(user -> {
                var accountOpt = response.getGetAccountList().stream()
                        .filter(account -> account.getId() == user.getId())
                        .findFirst();

                var accountDto = accountOpt
                        .map(account -> new AccountDto(account.getId(), account.getAccountNumber()))
                        .orElse(new AccountDto(0L, ""));

                return new GetRecentTransferListDto(
                        user.getId(),
                        new UserDto(user.getId(), user.getName()),
                        accountDto
                );
            })
            .toList();

        return ResponseEntity.ok(recentTransfers);
    }
}
