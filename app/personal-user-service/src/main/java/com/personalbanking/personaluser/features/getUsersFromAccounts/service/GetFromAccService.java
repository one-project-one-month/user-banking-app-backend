package com.personalbanking.personaluser.features.getUsersFromAccounts.service;

import com.personalbanking.personalUser.grpc.*;
import com.personalbanking.personaluser.features.getUsersFromAccounts.dto.FromAccountDataDto;
import com.personalbanking.personaluser.features.getUsersFromAccounts.dto.FromAccountOptionDto;
import com.personalbanking.personaluser.features.getUsersFromAccounts.repository.impl.GetFromAccRepositoryImpl;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@GrpcService
public class GetFromAccService extends GetFromAccountsServiceGrpc.GetFromAccountsServiceImplBase {

    private final GetFromAccRepositoryImpl repository;

    public GetFromAccService(GetFromAccRepositoryImpl repository) {
        this.repository = repository;
    }

    //Demo Hard code to retrieve the current logged in user id
    public int getCurrentLoggedInUserId() {
        return 1;
    }

    @Override
    public void getFromAccounts(GetFromAccountsRequest request, StreamObserver<GetFromAccountsResponse> responseObserver) {

        int userId = getCurrentLoggedInUserId();

        List<FromAccountOptionDto> fromAccountOptions = repository.findAllFromAccountsByUserId(userId);

        List<FromAccountOption> fromAccountOptionsGrpc = fromAccountOptions.stream().map(this::toFromAccountOptionsGrpc).toList();

        FromAccountData data = toFromAccountData(fromAccountOptionsGrpc);

        GetFromAccountsResponse response = toGetFromAccountsResponse(data);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    private GetFromAccountsResponse toGetFromAccountsResponse(FromAccountData data) {
        return GetFromAccountsResponse.newBuilder().setData(data).build();
    }

    private FromAccountData toFromAccountData(List<FromAccountOption> fromAccList) {
        return FromAccountData.newBuilder()
                .addAllFromAccountOptions(fromAccList)
                .build();
    }

    private FromAccountOption toFromAccountOptionsGrpc(FromAccountOptionDto dto) {
        return FromAccountOption.newBuilder()
                .setId(dto.id())
                .setAccountNumber(dto.accountNumber())
                .setBalance(dto.balance())
                .build();
    }


}
