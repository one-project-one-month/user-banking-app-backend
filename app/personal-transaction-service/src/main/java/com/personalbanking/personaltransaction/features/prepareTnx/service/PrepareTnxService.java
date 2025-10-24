package com.personalbanking.personaltransaction.features.prepareTnx.service;

import com.personalbanking.personaltransaction.features.nicknametransfer.models.Transaction;
import com.personalbanking.personaltransaction.features.prepareTnx.repo.PrepareTnxRepo;
import com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionRequest;
import com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionResponse;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@GrpcService
public class PrepareTnxService {
    private final PrepareTnxRepo prepareTnxRepo;
    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public PrepareTnxService(PrepareTnxRepo prepareTnxRepo) {
        this.prepareTnxRepo = prepareTnxRepo;
    }

    public void prepareTransaction(PrepareTransactionRequest request, StreamObserver<PrepareTransactionResponse> responseObserver) {

        try {
            if (request.getFromAccountId() <= 0 || request.getToAccountId() <= 0) {
                throw status(Status.INVALID_ARGUMENT, "Account IDs must be positive");
            }

            var fromAccountOpt = prepareTnxRepo.findAccountByUserId(request.getFromAccountId());
            var toAccountOpt = prepareTnxRepo.findAccountByUserId(request.getToAccountId());

            if (fromAccountOpt.isEmpty()) {
                throw status(Status.NOT_FOUND, "From account not found");
            }
            if (toAccountOpt.isEmpty()) {
                throw status(Status.NOT_FOUND, "To account not found");
            }

            if (fromAccountOpt.get().id().equals(toAccountOpt.get().id())) {
                throw status(Status.FAILED_PRECONDITION, "Cannot transfer to the same account");
            }

            String externalTxnId = prepareTnxRepo.prepareTransfer(
                    fromAccountOpt.get().id(),
                    toAccountOpt.get().id(),
                    null
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

            prepareTnxRepo.saveTransaction(txn);

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
