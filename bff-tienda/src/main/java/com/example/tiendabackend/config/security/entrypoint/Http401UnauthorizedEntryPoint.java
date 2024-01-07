package com.example.tiendabackend.config.security.entrypoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * AuthenticationEntryPoint en Spring Security se utiliza para manejar las excepciones de autenticación.
 * Cuando un usuario intenta acceder a un recurso protegido y no está autenticado,
 * se lanza una excepción AuthenticationException. La implementación por defecto de AuthenticationEntryPoint en Spring Security
 * es la LoginUrlAuthenticationEntryPoint  la cual maneja esta excepción redirigiendo
 * al usuario a la página de inicio de sesión configurada .login() la cual podría no ser la más adecuada
 * si estás construyendo una aplicación Angular. En una SPA, generalmente no rediriges a páginas de inicio de sesión tradicionales,
 * sino que manejas la autenticación de forma más dinámica a través de interacciones de la aplicación retornando un 401 por ej.
 */
@Component
public class Http401UnauthorizedEntryPoint implements AuthenticationEntryPoint {

    private static final Logger log = LoggerFactory.getLogger(Http401UnauthorizedEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Request to {} no autorizado: {}", request.getServletPath(), authException.getMessage());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }

}
