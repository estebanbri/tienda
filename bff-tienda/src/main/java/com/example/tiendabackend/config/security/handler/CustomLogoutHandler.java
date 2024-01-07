package com.example.tiendabackend.config.security.handler;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tiendabackend.component.JwtService;
import com.example.tiendabackend.component.TokenService;
import com.example.tiendabackend.enums.JwtScope;
import com.example.tiendabackend.service.UserService;
import com.example.tiendabackend.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomLogoutHandler implements LogoutHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomLogoutHandler.class);

    @Autowired
    private TokenService tokenService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("Start logout process");
        var jwt = WebUtils.extractJwtAccessToken(request);
        DecodedJWT decodedJWT;
        try {
            decodedJWT = jwtService.verifyAndDecodeJwt(jwt, JwtScope.READ_WRITE);
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setHeader("error-message", e.getMessage());
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } catch (IOException ex) {
                log.error(ex.getMessage());
                ex.printStackTrace();
            }
            return;
        }
        var username = decodedJWT.getSubject();
        var user = this.userService.findByUsernameOrEmailAccountActivated(username);
        tokenService.revokeAllUserTokens(user);
        WebUtils.deleteJwtCookies(response);
        log.info("End logout process");
    }
}
