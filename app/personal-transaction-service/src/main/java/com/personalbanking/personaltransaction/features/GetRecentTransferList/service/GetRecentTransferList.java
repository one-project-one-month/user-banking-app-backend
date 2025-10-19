package com.personalbanking.personaltransaction.features.GetRecentTransferList.service;

import com.personalbanking.GetRecentTransferList.GetRecentTransferListRequest;
import com.personalbanking.GetRecentTransferList.GetRecentTransferListResponse;
import com.personalbanking.GetRecentTransferList.GetUser;
import com.personalbanking.GetRecentTransferList.GetAccount;
import com.personalbanking.personaltransaction.features.GetRecentTransferList.model.GetRecentTransferListData;
import com.personalbanking.personaltransaction.features.GetRecentTransferList.repository.GetRecentTransferListJdbcRepo;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import com.personalbanking.GetRecentTransferList.GetRecentTransferListGrpc;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@GrpcService
public class GetRecentTransferList extends GetRecentTransferListGrpc.GetRecentTransferListImplBase{

    private final GetRecentTransferListJdbcRepo getRecentTransferListJdbcRepo;

    public GetRecentTransferList(GetRecentTransferListJdbcRepo getRecentTransferListJdbcRepo) {
        this.getRecentTransferListJdbcRepo = getRecentTransferListJdbcRepo;
    }

    @Override
    public void getRecentTransferList(GetRecentTransferListRequest request, StreamObserver<GetRecentTransferListResponse> responseObserver) {
        List<GetRecentTransferListData> getRecentTransferList = getRecentTransferListJdbcRepo.findAll();
        List<GetUser> users = getRecentTransferList.stream()
            .map(this::toUser)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        List<GetAccount> accounts = getRecentTransferList.stream()
            .map(this::toAccount)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

        GetRecentTransferListResponse response = GetRecentTransferListResponse.newBuilder()
            .addAllGetUser(users)
            .addAllGetAccount(accounts)
            .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
        }

    private GetUser toUser(GetRecentTransferListData data) {
        var modelUser = data.getUser();
        if (modelUser == null) return null;
        return GetUser.newBuilder()
            .setId(modelUser.id() == null ? 0L : modelUser.id())
            .setName(modelUser.name() == null ? "" : modelUser.name())
            .build();
        }

    private GetAccount toAccount(GetRecentTransferListData data) {
    var modelAccount = data.getAccount();
    if (modelAccount == null) return null;
    return GetAccount.newBuilder()
        .setId(modelAccount.id() == null ? 0L : modelAccount.id())
        .setAccountNumber(modelAccount.accountNumber() == null ? "" : modelAccount.accountNumber())
        .build();
    }
}
