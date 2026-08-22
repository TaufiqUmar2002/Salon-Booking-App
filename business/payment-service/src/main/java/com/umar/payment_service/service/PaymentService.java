package com.umar.payment_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.umar.exceptions.common.exception.ApiException;
import com.umar.payload.enums.booking.BookingStatus;
import com.umar.payload.enums.payment.PaymentStatus;
import com.umar.payload.request.payments.PaymentInitiationRequest;
import com.umar.payload.request.payments.fraud.FraudScoringRequest;
import com.umar.payload.request.payments.fraud.GatewayWebhookRequest;
import com.umar.payload.request.user.UserValidateResponse;
import com.umar.payload.response.booking.BookingResponseV1;
import com.umar.payload.response.payments.PaymentInitiationResponse;
import com.umar.payment_service.events.PaymentEventProducer;
import com.umar.payment_service.exchange.BookingClient;
import com.umar.payment_service.exchange.UserClient;
import com.umar.payment_service.model.PaymentRecord;
import com.umar.payment_service.model.PromoCodes;
import com.umar.payment_service.repository.PaymentRepository;
import com.umar.payment_service.repository.PromoCodeRepository;
import com.umar.payment_service.serviceInterface.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final UserClient userClient;
    private final BookingClient bookingClient;
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final ObjectMapper mapper;
    private final RazorpayWebhookVerifier verifier;
    private final PromoCodeRepository promoCodeRepository;



    @Override
    public PaymentInitiationResponse initiatePayment(PaymentInitiationRequest request) {
        UserValidateResponse userResponse = userClient.getUserValidation();
        BookingResponseV1 bookingResponse = bookingClient.getBookingById(request.getBookingId());
        if(!userResponse.getUserId().equals(bookingResponse.getUserId()) && !bookingResponse.getStatus().equals(BookingStatus.COMPLETED)){
            throw new ApiException(HttpStatus.BAD_REQUEST,"FORBIDDEN","Caller is not the owner of the booking");
        }
        Long totalPaymentExists = paymentRepository.paymentExistsByBookingId(request.getBookingId(), List.of(PaymentStatus.INITIATED,PaymentStatus.COMPLETED));
        if(totalPaymentExists>0){
            throw new ApiException(HttpStatus.BAD_REQUEST,"ALREADY_EXISTS","Payment already exists");
        }
        if(request.getPromoCode()!=null){
           this.applyDiscount(request.getPromoCode(), bookingResponse);
        }
        FraudScoringRequest fraudScoringRequest = FraudScoringRequest.builder()
                .bookingId(request.getBookingId())
                .currency(request.getCurrency())
                .userId(userResponse.getUserId())
                .amount(bookingResponse.getTotalPrice())
                .amount(bookingResponse.getTotalPrice())
                .paymentMethod(request.getPaymentMethod().name())
                .build();
        paymentEventProducer.publishFraudScoringRequest(fraudScoringRequest);
        /*
        fraud response receive
         */
        PaymentRecord paymentRecord = new PaymentRecord();
        double fraudScore = 0.0;
        if(fraudScore>0.7){
            paymentRecord.setStatus(PaymentStatus.FRAUD_FLAGGED);
            paymentRepository.save(paymentRecord);
        }
        paymentRecord.setStatus(PaymentStatus.INITIATED);
        paymentRepository.save(paymentRecord);
        return PaymentInitiationResponse.builder()
                .paymentRef(paymentRecord.getId().toString())
                .build();
    }

    @Override
    public void confirmPayment(String payload, String signature) {
        verifier.verify(payload, signature);
        GatewayWebhookRequest request = null;
        try {
            request = mapper.readValue(payload, GatewayWebhookRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        String gatewayPaymentId = request.getData();
        PaymentRecord payment =
                paymentRepository.findByPaymentRef(gatewayPaymentId)
                        .orElseThrow(() ->
                                new ApiException(HttpStatus.NOT_ACCEPTABLE,"","Payment not found"));

        if (payment.getStatus() == PaymentStatus.COMPLETED
                || payment.getStatus() == PaymentStatus.FAILED) {

            return;
        }

        String eventType = request.getData();

        switch (eventType) {

            case "payment.captured":

                handleSuccess(payment, request);

                break;

            case "payment.failed":

                handleFailure(payment, request);

                break;

            default:

                return;
        }

    }


    @Override
    public void refundPayment(Long bookingId) {

    }

    @Override
    public void getPaymentHistoryByUser(Long userId) {

    }


    private void handleSuccess(
            PaymentRecord payment,
            GatewayWebhookRequest request) {
//
//        var object = request.getData();
//
//        payment.setStatus(PaymentStatus.COMPLETED);
//
//        payment.setGatewayStatus(object.getStatus());
//
//        payment.setCardBrand(
//                object.getPaymentMethodDetails().getBrand());
//
//        payment.setCardLastFour(
//                object.getPaymentMethodDetails().getLast4());
//
//        repository.save(payment);
//
//        String receiptUrl =
//                receiptService.generateReceipt(payment);
//
//        payment.setReceiptUrl(receiptUrl);
//
//        repository.save(payment);
//
//        producer.publishPaymentCompleted(payment);

    }

    private void handleFailure(
            PaymentRecord payment,
            GatewayWebhookRequest request) {
//
//        payment.setStatus(PaymentStatus.FAILED);
//
//        payment.setGatewayStatus(
//                request.getData()
//                        .getObject()
//                        .getStatus());
//
//        repository.save(payment);
//
//        producer.publishPaymentFailed(payment);

    }

    private void  applyDiscount(String promoCodeStr, BookingResponseV1 bookingResponse){
        PromoCodes promoCode = promoCodeRepository.findByPromoCode(promoCodeStr).orElseThrow(() -> new ApiException(HttpStatus.BAD_REQUEST,"INVALID_PROMO_CODE","Invalid promo code"));
        if(!promoCode.isPromoActiveAndNotExpired()){
            throw new ApiException(HttpStatus.BAD_REQUEST,"INVALID_PROMO_CODE","Invalid promo code");
        }
        if(promoCode.getDiscountBasedOnFlag().equals("RATE")){
            bookingResponse.setTotalPrice(bookingResponse.getTotalPrice().subtract(bookingResponse.getTotalPrice().multiply(promoCode.getDiscountAmountOrRate())));
        }
        else{
            bookingResponse.setTotalPrice(bookingResponse.getTotalPrice().subtract(promoCode.getDiscountAmountOrRate()));
        }
    }

}
