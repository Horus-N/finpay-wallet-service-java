package com.finpay.wallet_service.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class WalletCreateRequest {
    private UUID userId;
}
