package com.example.tiendabackend.dto;

import lombok.Data;

@Data
public class ConfirmPaymentIntentDTO {
    private String paymentMethod;
    private String receiptEmail;
    private String returnUrl;
}
