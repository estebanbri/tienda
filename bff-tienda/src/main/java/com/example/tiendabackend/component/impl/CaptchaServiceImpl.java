package com.example.tiendabackend.component.impl;

import com.example.tiendabackend.component.CaptchaService;
import com.example.tiendabackend.config.properties.CaptchaProperties;
import com.example.tiendabackend.dto.RecaptchaRequestDTO;
import com.example.tiendabackend.dto.RecaptchaResponse;
import com.example.tiendabackend.exception.InvalidTokenException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger log = LoggerFactory.getLogger(CaptchaServiceImpl.class);

    @Autowired
    private CaptchaProperties captchaProperties;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public RecaptchaResponse verify(String userResponse) {
        RecaptchaResponse recaptchaResponse = null;

        UriComponentsBuilder urlBuilder =
                UriComponentsBuilder.fromHttpUrl(captchaProperties.getUrl())
                .queryParam("secret", captchaProperties.getSecret())
                .queryParam("response", userResponse);

        try {
            recaptchaResponse = restTemplate.postForObject(
                    urlBuilder.toUriString(),
                    HttpMethod.POST,
                    RecaptchaResponse.class
            );

            log.info("Captcha validation result {}", recaptchaResponse);

        } catch (HttpClientErrorException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        return recaptchaResponse;
    }
}
