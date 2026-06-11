package com.finpay.wallet_service.controller;


import com.finpay.wallet_service.entity.Wallet;
import com.finpay.wallet_service.model.dto.DepositRequest;
import com.finpay.wallet_service.model.dto.WalletCreateRequest;
import com.finpay.wallet_service.model.dto.WithdrawRequest;
import com.finpay.wallet_service.service.WalletServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/wallets")
public class WalletController {
    private final WalletServiceImpl walletService;

    public WalletController(WalletServiceImpl walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Wallet> createWallet(@RequestBody WalletCreateRequest request) {
        Wallet wallet = walletService.createWallet(request);
        return ResponseEntity.ok(wallet);
    }

    @PostMapping("/deposit")
    public ResponseEntity<Wallet> depositMoney(@RequestBody DepositRequest request){
        Wallet updateWallet = walletService.deposit(request);
        return ResponseEntity.ok(updateWallet);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Wallet> withdrawMoney(@RequestBody WithdrawRequest request){
        Wallet updateWallet = walletService.withdraw(request);
        return ResponseEntity.ok(updateWallet);
    }
}