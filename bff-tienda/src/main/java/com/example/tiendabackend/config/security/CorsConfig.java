package com.example.tiendabackend.config.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;


@Configuration
public class CorsConfig {

    private static final Logger log = LoggerFactory.getLogger(CorsConfig.class);

    private static final long MAX_AGE = 3600L; // 1 hora
    private static final int CORS_FILTER_ORDER = -102;

    @Value("${cors.allowed.origin:localhost}")
    private String allowedOrigin;

    /**
     * Nota allowedOrigins es el unico dominio que esta permitido hacer llamadas a mi backend
     * @return
     */
    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistration() {
        log.info("cors.allowed.origin {}", allowedOrigin);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin(this.allowedOrigin);
        config.setAllowedMethods(this.allowedMethods());
        config.setAllowedHeaders(this.allowedHeaders());
        config.setMaxAge(MAX_AGE);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(source));

        // should be set order to -100 because we need to CorsFilter before SpringSecurityFilter
        bean.setOrder(CORS_FILTER_ORDER);
        return bean;
    }

    private List<String> allowedMethods() {
        return List.of(HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.DELETE.name());
    }

    private List<String>  allowedHeaders() {
        return List.of(HttpHeaders.CONTENT_TYPE,
                HttpHeaders.ACCEPT,
                HttpHeaders.AUTHORIZATION);
    }
}
