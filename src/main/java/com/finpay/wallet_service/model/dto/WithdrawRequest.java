package com.finpay.wallet_service.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Setter @Getter
public class WithdrawRequest {
    private UUID userId;
    private BigDecimal amount;
}
