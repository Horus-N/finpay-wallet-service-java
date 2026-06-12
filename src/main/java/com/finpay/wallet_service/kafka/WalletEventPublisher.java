package com.finpay.wallet_service.kafka;

import com.finpay.wallet_service.model.dto.WalletDepositEvent;
import com.finpay.wallet_service.model.dto.WalletWithdrawEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WalletEventPublisher {
    // KafkaTemplate là công cụ Spring cung cấp sẵn để đẩy tin lên kafka Broker
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public WalletEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishDepositEvent(WalletDepositEvent event){
        try{
            //1. Chuyển Object thành chuỗi JSON
            String message = objectMapper.writeValueAsString(event);
            // 2. Gửi tin lên Topic tên là: wallet-v1-deposit-events
            kafkaTemplate.send("wallet-v1-deposit-events",event.getUserId().toString(),message)
                    .whenComplete((result, ex) -> {

                        if (ex != null) {
                            System.out.println(
                                    "Publish failed"+
                                    ex
                            );
                            return;
                        }

                        System.out.println(
                                "Publish success. topic="
                                        + result.getRecordMetadata().topic()
                                        + ", partition="
                                        + result.getRecordMetadata().partition()
                                        + ", offset="
                                        + result.getRecordMetadata().offset()
                        );
                    });
            System.out.println("-> [Kafka Producer] đã bắn event nạp tiền thành công lên kafka topic deposit"+ message);
        } catch (Exception ex){
            System.out.println("-> LỖi khi bắn evenet lên kafka topic: "+ ex.getMessage());
        }
    }
    public void publishWithdrawEvent(WalletWithdrawEvent event){
        try{
            //1. Chuyển Object thành chuỗi JSON
            String message = objectMapper.writeValueAsString(event);
            //2. Gửi tin lên Topic tên là: wallet-v1-withdraw-event
            kafkaTemplate.send("wallet-v1-withdraw-events",event.getUserId().toString(),message);
            System.out.println("-> [Kafka Producer] đã bắn event nạp tiền thành công lên kafka topic withdraw "+ message);
        } catch (Exception ex) {
            System.out.println("-> LỖi khi bắn evenet lên kafka topic: "+ ex.getMessage());
        }
    }
}
