package com.example.tiendabackend.util;

import org.springframework.http.ResponseCookie;

import javax.servlet.http.Cookie;

/**
 *  Nota: el valor de domain y path son importantes porque esa info la va a usar el browser es decir la informacion que les definas ahi
 *  va a viajar en el SET-COOKIE header del response pero el browser va a enviar en cada request la cookie en custion si y solo si
 *  el dominio y el path coincide con la ruta del request que esta solicitando. Ej:
 *  Si en el BE seteas domain("www.reddit.com").path("/foo") para que el browser pueda recibir la cookie y por otro lado attache
 *  dicha cookie a un request dado, la caracteristicas que debe tener el request es que venga de un dominio reddit.com y que el path
 *  que esté solicitando este dentro de /foo si no se cumple eso el browser no la va a recibir ni tampoco la va a enviar dicha cookie
 *  y el server por ende no la vas a ver.
 *  Nota el domain si o si tiene que contener dos puntos es decir es valido como domain www.reddit.com pero no es valido reddit.com
 */
public class CookieUtils {

    private CookieUtils() {}

    public static String createCookie(String cookieName, String value, int maxAgeInMillis) {
        return ResponseCookie.from(cookieName, value)
                .httpOnly(true)
                .secure(true) // el valor en true significa que solo esta cookie se puede usar sobre https a menos que el domain tenga valor 'localhost'
                .domain("localhost") // cuando tengas un dominio podes dedfinirle para que sea apliqcable a este dominio
                .path("/") // En todas las rutas de tu web
                .maxAge(calculateMilisAsSeconds(maxAgeInMillis))
                .build()
                .toString();
    }

    public static Cookie deleteCookie(String cookieName) {
        Cookie cookie = new Cookie(cookieName,null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        return cookie;
    }

    private static int calculateMilisAsSeconds(long ms) {
        return (int) (ms / 1000);
    }

}
