package com.personalbanking.gateway.controller.confirmTransfer;

import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.personalbanking.confirmTransfer.grpc.ConfirmTransferRequest;
import com.personalbanking.confirmTransfer.grpc.ConfirmTransferServiceGrpc.ConfirmTransferServiceBlockingStub;
import com.personalbanking.gateway.dto.confirmTransferDto.ConfirmTransferRequestDto;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;

@RestController
@RequestMapping("/api/v1/personal-banking/transfer")
public class ConfirmTransferController {

    @GrpcClient("personal-transaction-service")
    private ConfirmTransferServiceBlockingStub confirmTransfersBlockingStub;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmTransfer(
            @RequestBody ConfirmTransferRequestDto request) {
        try {

            ConfirmTransferRequest payload = ConfirmTransferRequest.newBuilder()
                    .setFromAccountId(request.fromAccountId())
                    .setToAccountId(request.toAccountId())
                    .setAmount(request.amount())
                    .setNote(request.note())
                    .setPin(request.pin())
                    .build();

            confirmTransfersBlockingStub.confirmTransfer(payload);

            return ResponseEntity.ok(Collections.emptyList());
        } catch (StatusRuntimeException e) {
            HttpStatus status;
            switch (e.getStatus().getCode()) {
                case NOT_FOUND -> status = HttpStatus.NOT_FOUND;
                case PERMISSION_DENIED -> status = HttpStatus.FORBIDDEN;
                case INVALID_ARGUMENT -> status = HttpStatus.BAD_REQUEST;
                case UNAVAILABLE -> status = HttpStatus.SERVICE_UNAVAILABLE;
                default -> status = HttpStatus.INTERNAL_SERVER_ERROR;
            }

            return ResponseEntity.status(status)
                    .body(e.getStatus().getDescription());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Unexpected error: " + e.getMessage());
        }
    }

}
