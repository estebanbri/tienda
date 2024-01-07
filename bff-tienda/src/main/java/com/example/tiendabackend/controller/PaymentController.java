package com.example.tiendabackend.controller;

import com.example.tiendabackend.controller.endpoints.ApiConstants;
import com.example.tiendabackend.dto.ConfirmPaymentIntentDTO;
import com.example.tiendabackend.dto.PaymentIntentDTO;
import com.example.tiendabackend.service.PaymentService;
import com.stripe.model.PaymentIntent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value= ApiConstants.V1)
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping(ApiConstants.STRIPE_PAYMENT_INTENTS_BASE)
    public ResponseEntity<String> createPaymentIntent(@RequestBody PaymentIntentDTO paymentIntentDTO) {
        PaymentIntent paymentIntent = paymentService.createPaymentIntent(paymentIntentDTO);
        String body = paymentIntent.toJson();
        return ResponseEntity.ok(body);
    }

    @GetMapping(ApiConstants.STRIPE_PAYMENT_INTENTS_BY_ID)
    public ResponseEntity<String> retrievePaymentIntent(@PathVariable String id) {
        PaymentIntent paymentIntent = paymentService.retrievePaymentIntent(id);
        String body = paymentIntent.toJson();
        return ResponseEntity.ok(body);
    }

    @PostMapping(ApiConstants.STRIPE_PAYMENT_INTENTS_CONFIRM)
    public ResponseEntity<String> confirmPaymentIntent(@PathVariable String id, @RequestBody ConfirmPaymentIntentDTO confirmPaymentIntentDTO){
        PaymentIntent paymentIntent = paymentService.confirmPaymentIntent(id, confirmPaymentIntentDTO);
        String body = paymentIntent.toJson();
        return ResponseEntity.ok(body);
    }

    @PostMapping(ApiConstants.STRIPE_PAYMENT_INTENTS_CANCEL)
    public ResponseEntity<String> cancelPaymentIntent(@PathVariable String id) {
        PaymentIntent paymentIntent = paymentService.cancelPaymentIntent(id);
        String body = paymentIntent.toJson();
        return ResponseEntity.ok(body);
    }

}
