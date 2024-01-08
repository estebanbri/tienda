package com.example.catalogoservice.infrastructure.config;

import com.example.catalogoservice.application.common.UseCase;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan(
        basePackages = "com.example.catalogoservice.application",
        includeFilters = @ComponentScan.Filter(type = FilterType.ANNOTATION, value = UseCase.class)
)
public class ApplicationConfig {

    @Bean // micrometer-tracing needs RestTemplateBuilder to add tracing data to every request
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder.build();
    }

}
