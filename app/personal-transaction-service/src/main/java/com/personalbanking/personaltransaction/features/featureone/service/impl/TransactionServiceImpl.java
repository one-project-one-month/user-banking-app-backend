package com.personalbanking.personaltransaction.features.featureone.service.impl;

import com.personalbanking.personaltransaction.features.featureone.repository.FeatureOneRepository;
import com.personalbanking.personaltransaction.features.featureone.service.FeatureOneService;
import com.personalbanking.personaltransaction.features.nicknametransfer.models.Transaction;
import com.personalbanking.personaltransaction.features.nicknametransfer.repository.NicknameTransferRepository;
import com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionRequest;
import com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionResponse;
import com.personalbanking.personaltransaction.proto.transaction.TransactionServiceGrpc;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@GrpcService
public class TransactionServiceImpl extends TransactionServiceGrpc.TransactionServiceImplBase {

    private final FeatureOneRepository featureOneRepository;
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public TransactionServiceImpl(FeatureOneRepository featureOneRepository) {
        this.featureOneRepository = featureOneRepository;
    }

    @Override
    public void prepareTransaction(PrepareTransactionRequest request, StreamObserver<PrepareTransactionResponse> responseObserver) {
        try {
            if (request.getFromAccountId() <= 0 || request.getToAccountId() <= 0) {
                throw status(Status.INVALID_ARGUMENT, "Account IDs must be positive");
            }

            var fromAccountOpt = featureOneRepository.findAccountByUserId(request.getFromAccountId());
            var toAccountOpt = featureOneRepository.findAccountByUserId(request.getToAccountId());

            if (fromAccountOpt.isEmpty()) {
                throw status(Status.NOT_FOUND, "From account not found");
            }
            if (toAccountOpt.isEmpty()) {
                throw status(Status.NOT_FOUND, "To account not found");
            }

            if (fromAccountOpt.get().id().equals(toAccountOpt.get().id())) {
                throw status(Status.FAILED_PRECONDITION, "Cannot transfer to the same account");
            }

            String externalTxnId = featureOneRepository.prepareTransfer(
                    fromAccountOpt.get().id(),
                    toAccountOpt.get().id(),
                    null // optional: current user ID if needed
            );

            var txn = new Transaction(
                    null,
                    toAccountOpt.get().id(),
                    fromAccountOpt.get().id(),
                    BigDecimal.valueOf(2000),
                    "PREPARED",
                    LocalDateTime.now().format(DT_FMT),
                    LocalDateTime.now().format(DT_FMT)
            );

            featureOneRepository.saveTransaction(txn);

            var response = PrepareTransactionResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage("Transaction prepared successfully")
                    .setTransactionId(externalTxnId)
                    .setStatus("PREPARED")
                    .build();

            responseObserver.onNext(response);
            responseObserver.onCompleted();

        } catch (StatusRuntimeException sre) {
            responseObserver.onError(sre);
        } catch (Exception e) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Internal server error")
                    .withCause(e)
                    .asRuntimeException());
        }
    }

    private StatusRuntimeException status(Status s, String msg) {
        return s.withDescription(msg).asRuntimeException();
    }


}
