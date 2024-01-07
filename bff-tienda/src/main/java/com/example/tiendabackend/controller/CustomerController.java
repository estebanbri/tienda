package com.example.tiendabackend.controller;

import com.example.tiendabackend.controller.endpoints.ApiConstants;
import com.example.tiendabackend.dto.CustomerDTO;
import com.example.tiendabackend.service.CustomerService;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value= ApiConstants.V1)
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(ApiConstants.STRIPE_CUSTOMERS_BASE)
    public ResponseEntity<String> createCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = customerService.createCustomer(customerDTO);
        String body = customer.toJson();
        return ResponseEntity.ok(body);
    }

    @GetMapping(ApiConstants.STRIPE_CUSTOMERS_BY_ID)
    public ResponseEntity<String> retrieveCustomer(@PathVariable String id) {
        Customer customer = customerService.retrieveCustomer(id);
        String body = customer.toJson();
        return ResponseEntity.ok(body);
    }

    @GetMapping(ApiConstants.STRIPE_CUSTOMER_SEARCH_EMAIL)
    public ResponseEntity<String> searchCustomerByEmail(@PathVariable String email) {
        Customer customer = customerService.searchByEmail(email);
        if (customer == null) return null;
        String body = customer.toJson();
        return ResponseEntity.ok(body);
    }

    @PostMapping(ApiConstants.STRIPE_CUSTOMERS_BY_ID)
    public ResponseEntity<String> updateCustomer(@PathVariable String id, @RequestBody Map<String, Object> valuesToUpdate) {
        Customer customer = customerService.updateCustomer(id, valuesToUpdate);
        String body = customer.toJson();
        return ResponseEntity.ok(body);
    }

}
