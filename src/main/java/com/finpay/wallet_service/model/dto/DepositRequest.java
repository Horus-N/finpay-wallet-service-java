package com.finpay.wallet_service.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
public class DepositRequest {
    private UUID userId;
    private BigDecimal amount;
}
