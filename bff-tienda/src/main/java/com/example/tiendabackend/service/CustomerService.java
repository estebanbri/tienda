package com.example.tiendabackend.service;

import com.example.tiendabackend.dto.CustomerDTO;
import com.stripe.model.Customer;

import java.util.Map;

public interface CustomerService {
    Customer retrieveCustomer(String paymentIntentDTO);
    Customer createCustomer(CustomerDTO paymentIntentDTO);
    Customer searchByEmail(String email);
    Customer updateCustomer(String customerId, Map<String, Object> valuesToUpdate);
}
