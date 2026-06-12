package com.finpay.wallet_service.service;

import com.finpay.wallet_service.entity.Wallet;
import com.finpay.wallet_service.kafka.WalletEventPublisher;
import com.finpay.wallet_service.model.dto.*;
import com.finpay.wallet_service.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class WalletServiceImpl {
    private final WalletRepository walletRepository;
    private  final WalletEventPublisher walletEventPublisher;
    public WalletServiceImpl(WalletRepository walletRepository, WalletEventPublisher walletEventPublisher) {
        this.walletRepository = walletRepository;
        this.walletEventPublisher = walletEventPublisher;
    }

    public Wallet createWallet(WalletCreateRequest request) {
        Wallet wallet = Wallet.builder()
                .userId(request.getUserId())
                .balance(BigDecimal.ZERO) // Ví mới tạo có 0 đồng
                .currency("VND")
                .build();
        walletRepository.save(wallet);
        return wallet;
    }
    @Transactional // BẮT BUỘC: KHÓA CHỈ CÓ TÁC DỤNG TRONG SUÔỐT 1 TRANSACTION ĐÓNG MỞ
    public Wallet deposit(DepositRequest request){
        // 1. Tìm ví và ĐẶT KHÓA dòng dữ liệu này lại ngăn chặn mọi luồng khác can thiệp
        Wallet wallet = walletRepository.findByUserIdWithLock(request.getUserId())
                .orElseThrow(()->new RuntimeException("The user's wallet was not found!"));
        //2. Kiểm tra số tiền nạp hợp lệ (Không được nhỏ hơn hoặc bằng 0)
        if(request.getAmount().compareTo(BigDecimal.ZERO)<=0){
            throw new IllegalArgumentException("The amount deposited must be greater than 0!");
        }
        //3. Tiến hành cộng tiền thông qua BigDecimal.add() để đảm bảo chính xác tuyệt đối
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        // 4. Lưu lại vào db (Sau khi hàm kết thúc, Transaction kết thúc, khóa sẽ tự động nhả ra
        Wallet savedWallet = walletRepository.save(wallet);
       // đưa về objs walletdepositevent
        WalletDepositEvent event = WalletDepositEvent.builder()
                .userId(savedWallet.getUserId())
                .amount(request.getAmount())
                .newBalance(savedWallet.getBalance())
                .timestamp(LocalDateTime.now().toString())
                .build();
        //5. Kích hoạt phát event sang kafka (chạy bất đồng bộ ngầm)
        walletEventPublisher.publishDepositEvent(event);
        return savedWallet;

    }

    @Transactional // Bắt buộc: khóa chỉ có tác dụng trong suốt 1 transaction đóng mở
    public Wallet withdraw(WithdrawRequest request){
        // 1. Tìm ví và đặt khóa dòng dữ liệu này lại ngăn chặn mọi luồn khác can thiệp
        Wallet wallet = walletRepository.findByUserIdWithLock(request.getUserId())
                .orElseThrow(()-> new RuntimeException("The user's wallet was not found!"));
        // 2. Kiểm tra số tiền rút hợp lệ (không được lớn hơn balance and bé hơn or bằng 0)
        if(request.getAmount().compareTo(wallet.getBalance())>0 || request.getAmount().compareTo(BigDecimal.ZERO)<=0){
            throw  new IllegalArgumentException("The amount withdraw must be greater than 0 or smaller than balance !");
        }
        // 3. Tiến hành trừ tiền thông qua bigdecimal.des() để đảm bảo chính xác tuyệt đối
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        // 4. Lưu vào db commit

        Wallet savedWallet = walletRepository.save(wallet);
        WalletWithdrawEvent walletWithdrawEvent = WalletWithdrawEvent.builder()
                .userId(savedWallet.getUserId())
                .amount(request.getAmount())
                .newBalance(savedWallet.getBalance())
                .timestamp(LocalDateTime.now().toString())
                .build();
        //5. Kích hoạt phát event sang kafka (chạy bất đồng bộ ngầm)
        walletEventPublisher.publishWithdrawEvent(walletWithdrawEvent);
        return savedWallet;
    }
}
