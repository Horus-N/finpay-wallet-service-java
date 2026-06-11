package com.finpay.wallet_service.controller;


import com.finpay.wallet_service.entity.Wallet;
import com.finpay.wallet_service.model.dto.WalletCreateRequest;
import com.finpay.wallet_service.repository.WalletRepository;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {
    private final WalletRepository walletRepository;

    public WalletController(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    @PostMapping
    public String createWallet(@RequestBody WalletCreateRequest request) {
        Wallet wallet = Wallet.builder()
                .userId(request.getUserId())
                .balance(BigDecimal.ZERO) // Ví mới tạo có 0 đồng
                .currency("VND")
                .build();
        walletRepository.save(wallet);
        return "Ví FinPay đã được khởi tạo cho user: " + request.getUserId();
    }
}