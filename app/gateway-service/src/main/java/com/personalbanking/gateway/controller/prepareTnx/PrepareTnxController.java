package com.personalbanking.gateway.controller.prepareTnx;

import com.personalbanking.gateway.dto.prepareTnx.TnxRequestDto;
import com.personalbanking.gateway.dto.prepareTnx.TnxResponseDto;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/personal-banking/users/prepare-transaction")
public class PrepareTnxController {

@GrpcClient("personal-transaction-service")
private com.personalbanking.personaltransaction.proto.transaction.TransactionServiceGrpc.TransactionServiceBlockingStub prepareTransactionService;

@PostMapping("{accountId}")
    public ResponseEntity<TnxResponseDto> prepareTnx(@RequestBody TnxRequestDto requestDto){
    com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionRequest request =  com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionRequest.newBuilder()
            .setFromAccountId(requestDto.fromAccountId())
            .setToAccountId(requestDto.toAccountId())
            .build();

   try{
       com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionResponse response = prepareTransactionService.prepareTransaction(request);
       TnxResponseDto responseDto = toDto(response);

       return ResponseEntity.ok(responseDto);
   }catch(StatusRuntimeException e){
       HttpStatus http = switch (e.getStatus().getCode()) {
           case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
           case NOT_FOUND -> HttpStatus.NOT_FOUND;
           case PERMISSION_DENIED, UNAUTHENTICATED -> HttpStatus.UNAUTHORIZED;
           case FAILED_PRECONDITION -> HttpStatus.CONFLICT;
           case UNAVAILABLE, DEADLINE_EXCEEDED -> HttpStatus.SERVICE_UNAVAILABLE;
           default -> HttpStatus.INTERNAL_SERVER_ERROR;
       };
       TnxResponseDto err = new TnxResponseDto(
               false,
               e.getStatus().getDescription() != null ? e.getStatus().getDescription() : "gRPC call failed",
               null,
               e.getStatus().getCode().name()
       );
       return ResponseEntity.status(http).body(err);
   }
}

    private TnxResponseDto toDto(com.personalbanking.personaltransaction.proto.transaction.PrepareTransactionResponse response){
        return new TnxResponseDto(
                response.getSuccess(),
                response.getMessage(),
                response.getTransactionId(),
                response.getStatus()
        );
    }
}
