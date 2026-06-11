package com.finpay.wallet_service.repository;

import com.finpay.wallet_service.entity.Wallet;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {
    //Tìm ví bằng UserId
    Optional<Wallet> findByUserId(UUID userId);
    //KỸ THUẬT VÀNG: @Lock với chế độ PESSIMISTIC_WRITE
    /*
    * Khi hàm này chạy, Spring Boot 4.x sẽ tự sinh câu lệnh SQL: SELECT ... FROM wallets
    * WHERE user_id = ? FOR UPDATE
    */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.userId = :userId")
    Optional<Wallet> findByUserIdWithLock(UUID userId);
}
