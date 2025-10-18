package com.personalbanking.gateway.controller.validateTransfer;

import com.personalbanking.gateway.dto.validateTrasnferDto.AccountTypeGrpcResponseDto;
import com.personalbanking.gateway.dto.validateTrasnferDto.ValidateTransferGrpcResponseDto;
import com.personalbanking.gateway.dto.validateTrasnferDto.ValidateTransferRequestDto;
import com.personalbanking.gateway.dto.validateTrasnferDto.ValidateTransferResponseDto;
import com.personalbanking.validateTransfer.grpc.GrpcValidateTransferResponse;
import com.personalbanking.validateTransfer.grpc.ValidateTransferRequest;
import com.personalbanking.validateTransfer.grpc.ValidateTransferResponse;
import com.personalbanking.validateTransfer.grpc.ValidateTransferServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/personal-banking/transfer")
public class ValidateTransferController {

    @GrpcClient("personal-transaction-service")
    private ValidateTransferServiceGrpc.ValidateTransferServiceBlockingStub validateTransferServiceBlockingStub;

    @PostMapping("/validate")
    public ResponseEntity<ValidateTransferResponseDto<ValidateTransferGrpcResponseDto>> checkingTransfer(@RequestBody ValidateTransferRequestDto requestDto){

        try{

            ValidateTransferRequest transferRequest = ValidateTransferRequest.newBuilder()
                    .setFromAccountId(requestDto.fromAccountId())
                    .setToAccountId(requestDto.toAccountId())
                    .build();

            GrpcValidateTransferResponse grpcResponse = validateTransferServiceBlockingStub.validateTransfer(transferRequest);

            ValidateTransferGrpcResponseDto fromDetailDto = toDto(grpcResponse.getFromAccountDetails());
            ValidateTransferGrpcResponseDto toDetailDto = toDto(grpcResponse.getToAccountDetails());

            Map<String, ValidateTransferGrpcResponseDto> fromAccountDetail = new HashMap<>();
            fromAccountDetail.put("FromAccountDetails",fromDetailDto);

            Map<String, ValidateTransferGrpcResponseDto> toAccountDetail = new HashMap<>();
            toAccountDetail.put("ToAccountDetails",toDetailDto);

            List<Map<String, ValidateTransferGrpcResponseDto>> responseList = new ArrayList<>();
            responseList.add(fromAccountDetail);
            responseList.add(toAccountDetail);

            ValidateTransferResponseDto<ValidateTransferGrpcResponseDto> validateTransferResponseDto= new ValidateTransferResponseDto<>(
                    HttpStatus.OK.value(),
                    "Successfully Fetching Account Details",
                    responseList
            );
            return ResponseEntity.ok(validateTransferResponseDto);

        } catch (io.grpc.StatusRuntimeException e){
            System.out.println("Exception From Grpc : " + e);
            HttpStatus status = mapGrpcStatusToHttp(e.getStatus().getCode());

            ValidateTransferResponseDto<ValidateTransferGrpcResponseDto> errorResponse = new ValidateTransferResponseDto<>(
                    status.value(),
                    e.getStatus().getDescription() == null ? "UnExcepted Error" : e.getStatus().getDescription(),
                    null
            );
            return ResponseEntity.status(status).body(errorResponse);
        }
    }

    private ValidateTransferGrpcResponseDto toDto(ValidateTransferResponse responseFromGrpc){
        AccountTypeGrpcResponseDto accountType = new AccountTypeGrpcResponseDto(
                responseFromGrpc.getAccountType().getName(),
                responseFromGrpc.getAccountType().getCode()
        );

        return new ValidateTransferGrpcResponseDto(
                responseFromGrpc.getAccountNumber(),
                responseFromGrpc.getEmail(),
                responseFromGrpc.getUsername(),
                responseFromGrpc.getCurrentBalance(),
                accountType
        );
    }

    private HttpStatus mapGrpcStatusToHttp(io.grpc.Status.Code code){
        return switch (code){
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case INVALID_ARGUMENT -> HttpStatus.BAD_REQUEST;
            case FAILED_PRECONDITION -> HttpStatus.CONFLICT;
            case UNAVAILABLE -> HttpStatus.SERVICE_UNAVAILABLE;
            case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }


}
