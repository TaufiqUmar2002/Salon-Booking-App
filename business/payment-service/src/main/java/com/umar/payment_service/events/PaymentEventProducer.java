package com.umar.payment_service.events;

import com.umar.payload.request.payments.fraud.FraudScoringRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publishFraudScoringRequest(FraudScoringRequest fraudScoringRequest){
        kafkaTemplate.send("payment.fraud.check", fraudScoringRequest);
    }
}
