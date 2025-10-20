package com.personalbanking.personaltransaction.features.confirmTransfer.service;

import java.math.BigDecimal;
import com.personalbanking.confirmTransfer.grpc.ConfirmTransferRequest;
import com.personalbanking.confirmTransfer.grpc.ConfirmTransferResponse;
import com.personalbanking.confirmTransfer.grpc.ConfirmTransferServiceGrpc.ConfirmTransferServiceImplBase;
import com.personalbanking.personaltransaction.features.confirmTransfer.repository.AccountDetailRepository;
import com.personalbanking.personaltransaction.features.confirmTransfer.repository.TransactionRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ConfirmTransferServiceImpl extends ConfirmTransferServiceImplBase {

    private final AccountDetailRepository accountDetailRepository;
    private final TransactionRepository transactionRepository;

    public ConfirmTransferServiceImpl(AccountDetailRepository accountDetailRepository,
            TransactionRepository transactionRepository) {
        this.accountDetailRepository = accountDetailRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void confirmTransfer(ConfirmTransferRequest request,
            StreamObserver<ConfirmTransferResponse> responseObserver) {

        try {
            var fromAccountOpt = accountDetailRepository.findById(request.getFromAccountId());
            if (fromAccountOpt.isEmpty()) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Sender account not found")
                        .asRuntimeException());
                return;
            }
            var toAccountOpt = accountDetailRepository.findById(request.getToAccountId());
            if (toAccountOpt.isEmpty()) {
                responseObserver.onError(Status.NOT_FOUND
                        .withDescription("Receiver account not found")
                        .asRuntimeException());
                return;
            }

            var fromAccount = fromAccountOpt.get();
            var toAccount = toAccountOpt.get();

            // TO DO: Implement bcrypt pin matching
            boolean pinMatch = fromAccount.pin().equals(request.getPin());

            if (!pinMatch) {
                responseObserver.onError(Status.PERMISSION_DENIED
                        .withDescription("Invalid PIN")
                        .asRuntimeException());
                return;
            }

            BigDecimal transferAmount = BigDecimal.valueOf(request.getAmount());

            if (fromAccount.currentBalance().compareTo(transferAmount) < 0) {
                responseObserver.onError(Status.FAILED_PRECONDITION
                        .withDescription("Insufficient balance")
                        .asRuntimeException());
                return;
            }

            BigDecimal newFromBalance = fromAccount.currentBalance().subtract(transferAmount);
            BigDecimal newToBalance = toAccount.currentBalance().add(transferAmount);

            accountDetailRepository.updateBalance(fromAccount.id(), newFromBalance);
            accountDetailRepository.updateBalance(toAccount.id(), newToBalance);

            transactionRepository.insertTransaction(
                    fromAccount.id(),
                    toAccount.id(),
                    transferAmount,
                    "COMPLETED",
                    request.getNote());

            ConfirmTransferResponse response =
                    ConfirmTransferResponse.newBuilder().build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.UNKNOWN
                            .withDescription(e.getMessage())
                            .asRuntimeException());
        }

    }

}
