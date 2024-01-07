package com.example.tiendabackend.config.security.filter;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tiendabackend.component.JwtService;
import com.example.tiendabackend.component.TokenService;
import com.example.tiendabackend.enums.JwtScope;
import com.example.tiendabackend.exception.JwtAccessTokenRequiredException;
import com.example.tiendabackend.entities.AppUser;
import com.example.tiendabackend.entities.Token;
import com.example.tiendabackend.service.AuthService;
import com.example.tiendabackend.service.UserService;
import com.example.tiendabackend.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    private final JwtService jwtService;
    private final TokenService tokenService;

    public JwtRequestFilter(JwtService jwtService, TokenService tokenService) {
        this.tokenService = tokenService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException {

        try {
            // 1. Extraemos jwt de header Authorization
            String jwt = WebUtils.extractJwtAccessToken(request); // Authorizacion bearer Path  o cookie 2 bearer

            if (jwt != null) {

                // 2. Decodificamos y validamos la firma a nivel de capa de aplicación
                DecodedJWT decodedJWT = this.tryVerifyAndDecodeJWT(jwt);

                // 3. Validamos el jwt a nivel de capa de datos db (expirado o revoked)
                if (!tokenService.isValid(jwt)) {
                    throw new JwtAccessTokenRequiredException("Token expirado o revocado a nivel de capa de datos");
                }

                // 4. Obtenemos la data embebida en el jwt
                String username = decodedJWT.getSubject();
                String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
                for (String rol : roles) {
                    grantedAuthorities.add(new SimpleGrantedAuthority(rol));
                }

                log.info("Processing request {}", request.getServletPath());

                // 4. Actualizamos el security context con los datos del usuario (en este punto el usuario ya hizo su /login
                // correspondiente con username y password por ende es seguro actualizar el context holder
                this.authService.updateSecurityContext(username, grantedAuthorities);
            }

            // 5. Continue with the filter chain
            filterChain.doFilter(request, response);
            // doFilter va a empezas a aplicar los demas filtros en caso de que la request sea a una ruta definida
            // como publica en los antmatchers spring security lo deja pasar de largo al controller y no lo frena en ningun filter de authenticacion.
            // Pero si el path del request lo tenes configurado como que requiere authorizacion entonces ahi el filtro de AuthenticationFilter
            // lo va a frenar al request y si no esta cargado el objeto Authentication en el SecurityContext para esa altura, dicho filtro
            // lanza la excepcion AuthenticationException y se ejecuta el AuthenticationEntryPoint es decir tu clase Http401UnauthorizedEntryPoint

        } catch (Exception e) {
            logger.error(e.getMessage());
            response.setHeader("error-message", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private DecodedJWT tryVerifyAndDecodeJWT(String jwt) {
        try {
            return this.jwtService.verifyAndDecodeJwt(jwt, JwtScope.READ_WRITE);
        } catch (TokenExpiredException e) {
            // clean up token model en caso de que este expirado
            var decodedJWT = this.jwtService.decodeJwt(jwt);
            String username = decodedJWT.getSubject();
            AppUser user = this.userService.findByUsernameOrEmailAccountActivated(username);
            log.error("Access token from {} has expired", user.getId());
            this.tokenService.revokeAllUserTokensByTokenType(user, Token.TokenType.ACCESS_TOKEN);
            throw e;
        }
    }


}
