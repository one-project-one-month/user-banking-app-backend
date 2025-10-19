package com.personalbanking.personaltransaction.features.validateTransfer.service;

import com.personalbanking.personaltransaction.features.validateTransfer.dto.ValidateTransferDto;
import com.personalbanking.personaltransaction.features.validateTransfer.exception.AccountNumberNotFoundException;
import com.personalbanking.personaltransaction.features.validateTransfer.repository.AccountDetailRepo;
import com.personalbanking.validateTransfer.grpc.ValidateTransferRequest;
import com.personalbanking.validateTransfer.grpc.ValidateTransferResponse;
import com.personalbanking.validateTransfer.grpc.AccountTypeResponse;
import com.personalbanking.validateTransfer.grpc.GrpcValidateTransferResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.math.BigDecimal;

@GrpcService
public class ValidateTransferServiceImpl extends com.personalbanking.validateTransfer.grpc.ValidateTransferServiceGrpc.ValidateTransferServiceImplBase {

    private final AccountDetailRepo accountDetailRepo;

    public ValidateTransferServiceImpl(AccountDetailRepo accountDetailRepo){
       this.accountDetailRepo = accountDetailRepo;
    }

    @Override
    public void validateTransfer(ValidateTransferRequest request, StreamObserver<GrpcValidateTransferResponse> responseObserver) {

        try{
//            System.out.println("Gprc port is connected");
            ValidateTransferDto fromAcccountDto =  accountDetailRepo.findyByAccountNumber(request.getFromAccountId())
                    .orElseThrow(() -> new AccountNumberNotFoundException("From Account Number " + request.getToAccountId() + " is not found"));

            ValidateTransferDto toAcccountDto =  accountDetailRepo.findyByAccountNumber(request.getToAccountId())
                    .orElseThrow(() -> new AccountNumberNotFoundException("From Account Number " + request.getToAccountId() + " is not found"));

            ValidateTransferResponse fromAccountResponse = this.convertValidateTransferResponse(fromAcccountDto);
            ValidateTransferResponse toAccountResponse = this.convertValidateTransferResponse(toAcccountDto);

            GrpcValidateTransferResponse grpcValidateTransferResponse = GrpcValidateTransferResponse.newBuilder()
                    .setFromAccountDetails(fromAccountResponse)
                    .setToAccountDetails(toAccountResponse)
                    .build();

//            System.out.println("Grpc return Data");
            responseObserver.onNext(grpcValidateTransferResponse);
            responseObserver.onCompleted();

        } catch (AccountNumberNotFoundException e){
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        } catch (Exception e) {
            responseObserver.onError(
                    Status.UNKNOWN
                            .withDescription(e.getMessage())
                            .asRuntimeException()
            );
        }

    }

    private ValidateTransferResponse convertValidateTransferResponse(ValidateTransferDto validateTransferDto){

        AccountTypeResponse accountTypeResponse = AccountTypeResponse.newBuilder()
                .setName(validateTransferDto.accountType().name())
                .setCode(validateTransferDto.accountType().code())
                .build();

        return ValidateTransferResponse.newBuilder()
                .setAccountNumber(validateTransferDto.accountNumber())
                .setEmail(validateTransferDto.email())
                .setUsername(validateTransferDto.username())
                .setCurrentBalance(validateTransferDto.currentBalance().movePointRight(2).longValueExact())
                .setAccountType(accountTypeResponse)
                .build();

    }

}
