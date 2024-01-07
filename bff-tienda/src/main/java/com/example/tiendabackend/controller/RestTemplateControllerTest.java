package com.example.tiendabackend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class RestTemplateControllerTest {

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/hello")
    public String hello() {
        ResponseEntity<String> response = this.restTemplate.postForEntity("https://httpbin.org/post", "Hello, Cloud!", String.class);
        return response.getBody();
    }

}
