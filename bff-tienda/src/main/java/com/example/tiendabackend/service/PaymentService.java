package com.example.tiendabackend.service;

import com.example.tiendabackend.dto.ConfirmPaymentIntentDTO;
import com.example.tiendabackend.dto.PaymentIntentDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {
    PaymentIntent createPaymentIntent(PaymentIntentDTO paymentIntentDTO);
    PaymentIntent confirmPaymentIntent(String paymentIntentId, ConfirmPaymentIntentDTO confirmPaymentIntentDTO);
    PaymentIntent cancelPaymentIntent(String paymentIntentId);
    PaymentIntent retrievePaymentIntent(String paymentIntentId);
}
