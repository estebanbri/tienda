package com.example.tiendabackend.service.impl;

import com.example.tiendabackend.dto.CustomerDTO;
import com.example.tiendabackend.exception.ApiStripeException;
import com.example.tiendabackend.service.CustomerService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.CustomerListParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeCustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(StripeCustomerServiceImpl.class);

    @Override
    public Customer createCustomer(CustomerDTO customerDTO)  {

        CustomerCreateParams params = CustomerCreateParams.builder()
                .setEmail(customerDTO.getEmail())
                .setDescription(customerDTO.getDescription())
                .setName(customerDTO.getName())
                .build();

        try {
            return Customer.create(params);
        } catch (StripeException e) {
            log.error("Surgio un error al intentar crear un customer email: {}", customerDTO.getEmail(), e);
            throw new ApiStripeException(e.getMessage());
        }
    }

    @Override
    public Customer searchByEmail(String email)  {
        try {
            return Customer.list(CustomerListParams.builder().setEmail(email).setLimit(1L).build()).getData().stream()
                    .findFirst()
                    .orElse(null);
        } catch (StripeException e) {
            log.error("Surgio un error al intentar buscar por email un customer email: {}", email, e);
            throw new ApiStripeException(e.getMessage());
        }
    }

    @Override
    public Customer retrieveCustomer(String customerId) {
        try {
            return Customer.retrieve(customerId);
        } catch (StripeException e) {
            log.error("Surgio un error al intentar retornar un customer id: {}", customerId, e);
            throw new ApiStripeException(e.getMessage());
        }
    }

    @Override
    public Customer updateCustomer(String customerId, Map<String, Object> valuesToUpdate)  {
        var customer = this.retrieveCustomer(customerId);
        try {
            return customer.update(valuesToUpdate);
        } catch (StripeException e) {
            log.error("Surgio un error al intentar actualizar un customer id: {}", customerId, e);
            throw new ApiStripeException(e.getMessage());
        }
    }


}
