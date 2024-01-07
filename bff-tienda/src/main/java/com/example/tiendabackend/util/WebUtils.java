package com.example.tiendabackend.util;

import com.example.tiendabackend.controller.endpoints.ApiConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

import static com.example.tiendabackend.controller.endpoints.ApiConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.example.tiendabackend.controller.endpoints.ApiConstants.REFRESH_TOKEN_COOKIE_NAME;

public class WebUtils {

    private WebUtils() {}

    private static final String BEARER = "Bearer";
    public static final String BLANK_SPACE = " ";

    /**
     *  Extrae el JWT del request. Para el caso de cookies como viajan en cada request ambos tanto el
     *  access_token como refresh_token (asumiendo que no expiraron) hay que filtrar de las cookies segun el
     *  nombre de cookie access_token. Pero para el caso de via header Authentication no tenes contexto de cual
     *  agarrarte para saber si el jwt recibido es un access_token o refresh_token. Si bien se podria dotar de esta logica
     *  agregandole una capa de seguridad al parseo de tokens, por ejemplo se podria implementar
     *  agregando algun claim nuevo que almacene el tipo de token si es "access_token" o "refresh_token" esto hace sumar más
     *  complejidad y carga al servidor. Es decir perdemos las caracteristicas de flexibilidad y agilidad en el server por aumentar
     *  esta seguridad. Entonces asumimos ese supuesto riesgo de que el que se encarga de enviar el token correcto es el front end.
     * @param request
     * @return
     */
    public static String extractJwtAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(extractJwtFromCookie(request, ApiConstants.ACCESS_TOKEN_COOKIE_NAME))
                        .orElse(extractJwtFromAuthorizationHeader(request));
    }

    public static String extractJwtFromAuthorizationHeader(HttpServletRequest request) {
        var authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) ) {
            String[] autorizationHeaderTokens = authorizationHeader.split(BLANK_SPACE);
            if(autorizationHeaderTokens.length == 2 && BEARER.equals(autorizationHeaderTokens[0])) {
                return autorizationHeaderTokens[1];
            }
        }
        return null;
    }

    public static String extractJwtRefreshTokenFromCookie(HttpServletRequest request) {
        Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, ApiConstants.REFRESH_TOKEN_COOKIE_NAME);
        return cookie != null ? cookie.getValue() : null;
    }

    private static String extractJwtFromCookie(HttpServletRequest request, String cookieName) {
        Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, cookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    public static void deleteJwtCookies(HttpServletResponse response) {
        response.addCookie(CookieUtils.deleteCookie(ACCESS_TOKEN_COOKIE_NAME));
        response.addCookie(CookieUtils.deleteCookie(REFRESH_TOKEN_COOKIE_NAME));
    }
}
