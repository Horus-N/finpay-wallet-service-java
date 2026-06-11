package com.finpay.wallet_service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name="wallets")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(nullable = false,unique = true)
    private UUID userId; //linking with userid of user-service
    @Column(nullable = false)
    private BigDecimal balance; //Tiền tng hệ thống tài chính luôn dung BigDecimal
    @Column(nullable = false, length = 3)
    private String currency; //VND, USD...
}
