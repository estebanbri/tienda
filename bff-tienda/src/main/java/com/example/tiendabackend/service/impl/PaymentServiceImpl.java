package com.example.tiendabackend.service.impl;

import com.example.tiendabackend.dto.ConfirmPaymentIntentDTO;
import com.example.tiendabackend.dto.PaymentIntentDTO;
import com.example.tiendabackend.exception.ApiStripeException;
import com.example.tiendabackend.component.impl.mapper.CartItemMapper;
import com.example.tiendabackend.model.Cart;
import com.example.tiendabackend.service.PaymentService;
import com.example.tiendabackend.util.CartUtils;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class PaymentServiceImpl implements PaymentService, InitializingBean {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);

    @Value("${stripe.key.private}")
    private String stripePrivateKey;

    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public void afterPropertiesSet() {
        Stripe.apiKey = stripePrivateKey;
    }

    @Override
    public PaymentIntent createPaymentIntent(PaymentIntentDTO paymentIntentDTO) {
        PaymentIntent paymentIntent = null;
        var cartItems = this.cartItemMapper.mapToCartItems(paymentIntentDTO.getCartItemsDTOs());

        var cart = new Cart();
        cart.setCartItemLists(cartItems);
        cart.setAmount(CartUtils.calculateTotalAmount((cart)));
        // TODO save to db cart

        var params = PaymentIntentCreateParams.builder()
            .setAmount(cart.getAmount())
            .setCurrency(paymentIntentDTO.getCurrency())
            .setDescription(paymentIntentDTO.getDescription())
            .setCustomer(paymentIntentDTO.getCustomer())
            //.setSetupFutureUsage(PaymentIntentCreateParams.SetupFutureUsage.ON_SESSION) Permite guardar metodo de pago en el customer
            .build();

        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            log.error("Ocurrio un error al intentar crear un payment intent for customer: {}", paymentIntentDTO.getCustomer(), e);
            throw new ApiStripeException(e.getMessage());
        }
        log.info("Payment intent created {}", paymentIntent.getId());
        return paymentIntent;
    }



    @Override
    public PaymentIntent confirmPaymentIntent(String paymentIntentId, ConfirmPaymentIntentDTO confirmPaymentIntentDTO){

        PaymentIntent paymentIntent = null;

        var params = PaymentIntentConfirmParams.builder()
            .setPaymentMethod(confirmPaymentIntentDTO.getPaymentMethod())
            .setReceiptEmail(confirmPaymentIntentDTO.getReceiptEmail())
            .build();

        try {
            paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent = paymentIntent.confirm(params);
            log.info("Payment intent confirmed {}", paymentIntent.getId());
        } catch (StripeException e) {
            log.error("Surgio un error al intentar confirmar un payment intent id: {}", paymentIntentId, e);
            throw new ApiStripeException(e.getMessage());
        }
        return paymentIntent;
    }

    @Override
    public PaymentIntent cancelPaymentIntent(String paymentIntentId) {
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            paymentIntent = paymentIntent.cancel();
            log.info("Payment intent cancelled {}", paymentIntent.getId());
        } catch (StripeException e) {
            log.error("Surgio un error al intentar cancelar un payment intent id: {}", paymentIntentId, e);
            throw new ApiStripeException(e.getMessage());
        }
        return paymentIntent;
    }

    @Override
    public PaymentIntent retrievePaymentIntent(String paymentIntentId) {
        PaymentIntent paymentIntent = null;
        try {
            paymentIntent = PaymentIntent.retrieve(paymentIntentId);
            log.info("Payment intent retrieved {}", paymentIntent.getId());
        } catch (StripeException e) {
            log.error("Surgio un error al intentar retornar un payment intent id: {}", paymentIntentId, e);
            throw new ApiStripeException(e.getMessage());
        }
        return paymentIntent;
    }
}
