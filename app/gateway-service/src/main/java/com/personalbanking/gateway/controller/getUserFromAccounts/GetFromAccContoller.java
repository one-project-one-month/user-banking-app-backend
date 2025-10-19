package com.personalbanking.gateway.controller.getUserFromAccounts;

import com.personalbanking.gateway.dto.getUserFromAccounts.FromAccountDataDto;
import com.personalbanking.gateway.dto.getUserFromAccounts.FromAccountOptionDto;
import com.personalbanking.gateway.dto.getUserFromAccounts.GetFromAccountResponseDto;
import com.personalbanking.personalUser.grpc.*;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/personal-banking/users")
public class GetFromAccContoller {

    @GrpcClient("personal-user-service")
    private GetFromAccountsServiceGrpc.GetFromAccountsServiceBlockingStub serviceBlockingStub;

    @GetMapping("/from-accounts")
    public ResponseEntity<GetFromAccountResponseDto> getUsersFromAccounts() {
        return ResponseEntity.ok(getFromAccountResponseDto());
    }

    private GetFromAccountResponseDto getFromAccountResponseDto() {
        GetFromAccountsRequest request = GetFromAccountsRequest.newBuilder().build();
        GetFromAccountsResponse response = serviceBlockingStub.getFromAccounts(request);

        FromAccountDataDto fromAccountDataDto = new FromAccountDataDto( extractAccountOptions(response));
        return new GetFromAccountResponseDto(fromAccountDataDto);
    }

    private List<FromAccountOptionDto> extractAccountOptions(GetFromAccountsResponse response) {

        FromAccountData fromAccountData = response.getData();
        List<FromAccountOption> fromAccountOptions = fromAccountData.getFromAccountOptionsList();
        return fromAccountOptions.stream()
                .map(this::toFromAccontOptionDto).toList();
    }

    private FromAccountOptionDto toFromAccontOptionDto(FromAccountOption fromAccountOption) {
        return new FromAccountOptionDto(
                fromAccountOption.getId(),
                fromAccountOption.getAccountNumber(),
                fromAccountOption.getBalance()
        );
    }
}
