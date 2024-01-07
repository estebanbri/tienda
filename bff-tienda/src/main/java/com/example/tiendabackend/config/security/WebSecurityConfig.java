package com.example.tiendabackend.config.security;

import com.example.tiendabackend.component.JwtService;
import com.example.tiendabackend.component.TokenService;
import com.example.tiendabackend.config.security.entrypoint.Http401UnauthorizedEntryPoint;
import com.example.tiendabackend.config.security.filter.JwtRequestFilter;
import com.example.tiendabackend.config.security.handler.CustomLogoutHandler;
import com.example.tiendabackend.config.security.userdetails.CustomUserDetailsService;
import com.example.tiendabackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    private Http401UnauthorizedEntryPoint http401UnauthorizedEntryPoint;

    @Autowired
    private CustomLogoutHandler customLogoutHandler;

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

            http
                .cors()
                .and()
                .csrf().disable()
                .headers(headers -> headers.frameOptions().disable()) // remover en prod. esto hace fix H2 database console: Refused to display ' in a frame because it set 'X-Frame-Options' to 'deny'
                .authorizeHttpRequests(requests  ->
                        requests.antMatchers("/api/v1/auth/**").permitAll()
                                .antMatchers("/api/v1/catalogo").permitAll()
                                .antMatchers("/api/v1/config").permitAll()
                                .antMatchers("/h2-console/**").permitAll()
                                .antMatchers("/api/v1/csrf").permitAll()
                                .antMatchers("/**/favicon.ico").permitAll()
                                .antMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                                .antMatchers(HttpMethod.OPTIONS).permitAll() // CORS utiliza un preflight de request OPTIONS asi que tenes que habilitarlo
                                .anyRequest().authenticated())
                .exceptionHandling(handler -> handler.authenticationEntryPoint(http401UnauthorizedEntryPoint)) // unauthorize handler (si no tiene token y con login incorrecto se ejecuta este handler)
                .sessionManagement(sessionManager -> sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .anonymous().disable()
                .logout()
                    .logoutUrl("/api/v1/auth/logout")
                    .addLogoutHandler(customLogoutHandler)
                    .logoutSuccessHandler(((request, response, authentication) -> SecurityContextHolder.clearContext()));
        // Agregamos nuestro filtro JwtRequestFilter va a ser ejecutado con cada uno de los request,
        // y lo ubicamos antes del filtro por defecto de login con username y password que usa spring security UsernamePasswordAuthenticationFilter
        // porque ahora vos vas a realizar authenticar manualmente al usuario
         http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);
         return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(getPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter(jwtService, tokenService);
    }
}
