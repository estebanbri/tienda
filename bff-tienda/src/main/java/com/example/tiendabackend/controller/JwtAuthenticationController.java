package com.example.tiendabackend.controller;

import com.example.tiendabackend.component.CaptchaService;
import com.example.tiendabackend.component.JwtService;
import com.example.tiendabackend.component.TokenService;
import com.example.tiendabackend.controller.endpoints.ApiConstants;
import com.example.tiendabackend.dto.*;

import com.example.tiendabackend.service.AuthService;
import com.example.tiendabackend.service.UserService;

import com.example.tiendabackend.util.CookieUtils;
import com.example.tiendabackend.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import java.net.URI;

import static com.example.tiendabackend.controller.endpoints.ApiConstants.ACCESS_TOKEN_COOKIE_NAME;
import static com.example.tiendabackend.controller.endpoints.ApiConstants.REFRESH_TOKEN_COOKIE_NAME;

@RestController
@RequestMapping(value= ApiConstants.V1)
public class JwtAuthenticationController {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationController.class);

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserService userService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private CaptchaService captchaService;


    @Value("${jwt.access_token.expiration.ms}")
    private int jwtAccessTokenExpirationMs;

    @Value("${jwt.refresh_token.expiration.ms}")
    private int jwtRefreshTokenExpirationMs;

    @PostMapping(ApiConstants.LOGIN)
    public ResponseEntity<JwtDTO> authenticate(@RequestParam("username") String username,
                                               @RequestParam("password") String password) {
        JwtPairDTO jwtPair = this.authService.login(username, password);
        String refreshTokenCookie = CookieUtils.createCookie(REFRESH_TOKEN_COOKIE_NAME, jwtPair.getRefreshToken(), jwtRefreshTokenExpirationMs);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                .body(new JwtDTO(jwtPair.getAccessToken()));
    }

    @PostMapping(ApiConstants.LOGIN_WITH_BODY_ONLY)
    public ResponseEntity<JwtPairDTO> authenticateAndReturnBody(@RequestParam("username") String username,
                                                                @RequestParam("password") String password) {
        return ResponseEntity.ok(this.authService.login(username, password));
    }

    @PostMapping(ApiConstants.LOGIN_WITH_COOKIE_ONLY)
    public ResponseEntity<Void> authenticateAndReturnCookie(@RequestParam("username") String username,
                                                            @RequestParam("password") String password) {
        JwtPairDTO jwtPair = this.authService.login(username, password);
        String accessTokenCookie = CookieUtils.createCookie(ACCESS_TOKEN_COOKIE_NAME, jwtPair.getAccessToken(), jwtAccessTokenExpirationMs);
        String refreshTokenCookie = CookieUtils.createCookie(REFRESH_TOKEN_COOKIE_NAME, jwtPair.getRefreshToken(), jwtRefreshTokenExpirationMs);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie)
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie)
                .build();
    }

    @PostMapping(ApiConstants.SIGNUP)
    public ResponseEntity<Void> signUp(@Valid @RequestBody SignUpDTO signUpDTO) {
        authService.signUp(signUpDTO);
        return ResponseEntity.created(URI.create(ApiConstants.USERS_ME)).build();
    }

    @PostMapping(ApiConstants.ACCOUNT_ACTIVATE_ME)
    public ResponseEntity<Void> activateUserAccount(@Valid @RequestBody ActionRequestedByDTOWithTokenDTO actionRequestedByWithTokenDTO) {
        authService.activateUserAccount(actionRequestedByWithTokenDTO);
        return ResponseEntity.ok().build();
    }

    // TODO mover a otro controller
    @PostMapping(ApiConstants.PASSWORD_CHANGE_INTENT)
    public ResponseEntity<Void> passwordResetIntent(@Valid @RequestBody ActionRequestedByDTO actionRequestedByDTO) {
        authService.passwordResetRequest(actionRequestedByDTO);
        return ResponseEntity.ok().build();
    }

    // TODO mover a otro controller
    @PostMapping(ApiConstants.PASSWORD_CHANGE_VALIDATE_TOKEN)
    public ResponseEntity<Void> passwordResetValidateToken(@Valid @RequestBody ActionRequestedByDTOWithTokenDTO actionRequestedByWithTokenDTO) {
        authService.passwordResetTokenValidation(actionRequestedByWithTokenDTO);
        return ResponseEntity.ok().build();
    }

    // TODO mover a otro controller
    @PostMapping(ApiConstants.PASSWORD_CHANGE_CONFIRM)
    public ResponseEntity<Void> passwordResetConfirm(@Valid @RequestBody PasswordChangeConfirmDTO passwordChangeConfirmDTO) {
        authService.passwordResetConfirm(passwordChangeConfirmDTO);
        return ResponseEntity.ok().build();
    }

    // TODO mover a otro controller
    @GetMapping(ApiConstants.REFRESH_TOKEN)
    public ResponseEntity<JwtDTO> executeRefreshTokenProcess(HttpServletRequest request, HttpServletResponse response)  {
        String refreshToken = WebUtils.extractJwtRefreshTokenFromCookie(request);
        return ResponseEntity.ok(this.authService.renewAccessToken(refreshToken));
    }

    // TODO mover a otro controller
    @GetMapping(ApiConstants.REFRESH_TOKEN_WITH_BODY)
    public ResponseEntity<JwtPairDTO> executeRefreshTokenProcessWithBody(HttpServletRequest request, HttpServletResponse response)  {
        String refreshToken = WebUtils.extractJwtFromAuthorizationHeader(request);
        return ResponseEntity.ok(this.authService.renewAccessTokenPair(refreshToken));
    }

    // TODO mover a otro controller
    @GetMapping(ApiConstants.REFRESH_TOKEN_WITH_COOKIE)
    public ResponseEntity<Void> executeRefreshTokenProcessWithCookie(HttpServletRequest request, HttpServletResponse response)  {
        String refreshToken = WebUtils.extractJwtRefreshTokenFromCookie(request);
        JwtPairDTO accessTokenPair = this.authService.renewAccessTokenPair(refreshToken);
        String accessTokenCookie = CookieUtils.createCookie(ACCESS_TOKEN_COOKIE_NAME, accessTokenPair.getAccessToken(), jwtAccessTokenExpirationMs);
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie)
                .build();
    }

    // TODO mover a otro controller
    @PostMapping(ApiConstants.VERIFY_CAPTCHA)
    public ResponseEntity<Boolean> verifyCaptcha(@RequestBody String captchaToken) {
        return ResponseEntity.ok(captchaService.verify(captchaToken).isSuccess());
    }

}
