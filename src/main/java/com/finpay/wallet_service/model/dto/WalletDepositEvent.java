package com.finpay.wallet_service.model.dto;

import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletDepositEvent {
    private UUID userId;
    private BigDecimal amount;
    private BigDecimal newBalance;
    private String timestamp;
}
