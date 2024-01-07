package com.example.tiendabackend.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.tiendabackend.component.TokenService;
import com.example.tiendabackend.dto.*;
import com.example.tiendabackend.enums.JwtScope;
import com.example.tiendabackend.exception.*;
import com.example.tiendabackend.entities.Token;
import com.example.tiendabackend.entities.AppUser;
import com.example.tiendabackend.service.AuthService;
import com.example.tiendabackend.service.EmailService;
import com.example.tiendabackend.component.JwtService;
import com.example.tiendabackend.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Qualifier("accountActivationEmailSender")
    private EmailService accountActivationEmailSender;

    @Autowired
    @Qualifier("emailSenderPasswordResetConfirm")
    private EmailService emailSenderPasswordResetConfirm;

    @Autowired
    private TokenService tokenService;

    @Override
    public JwtPairDTO login(String username, String password) {
        log.info("Start login process for username:{}", username);

        // 1. Autentico al usuario
        authenticate(username, password);

        // 2. Buscamos el user en nuestro modelo de datos
        var user = this.userService.findByUsernameOrEmailAccountActivated(username);

        // 3. Generamos par de jwt access y refresh
        String accessToken = this.jwtService.generateAccessToken(user);
        String refreshToken = this.jwtService.generateRefreshToken(user);

        // 4. Guardamos en nuestro modelo de datos los nuevos tokens jwt access y refresh del usuario
        List<Token> tokens = Arrays.asList(new Token(accessToken, Token.TokenType.ACCESS_TOKEN, user),
                new Token(refreshToken, Token.TokenType.REFRESH_TOKEN, user));
        this.tokenService.saveAll(tokens);

        log.info("Finish login process for username:{}", username);
        return new JwtPairDTO(accessToken, refreshToken);
    }

    @Override
    public void signUp(SignUpDTO signUpDTO) {
        log.info("Start executing signup intent process for username:{}", signUpDTO.getUsername());
        AppUser user;
        try {
            user = this.userService.findByUsernameOrEmailAccountActivated(signUpDTO.getUsername());
            if(user.isAccountActivated()) {
                throw new AccountAlreadyActivatedException("Account already activated for username:" + signUpDTO.getUsername());
            }
            log.info("User will be update for username: {}", signUpDTO.getUsername());
        } catch (UserNotFoundException e) {
            log.info("New user will be created for username: {}", signUpDTO.getUsername());
            user = new AppUser();
        }
        BeanUtils.copyProperties(signUpDTO, user);
        user.setRole(AppUser.Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        var token = this.tokenService.generateRandomToken();
        this.userService.save(user);
        this.tokenService.save(new Token(token, Token.TokenType.ACCOUNT_ACTIVATION, user));
        this.accountActivationEmailSender.send(user, token);
        log.info("Finish executing signup intent process for username:{}", signUpDTO.getUsername());
    }

    @Override
    public void activateUserAccount(ActionRequestedByDTOWithTokenDTO actionRequestedByWithTokenDTO) {
        Objects.requireNonNull(actionRequestedByWithTokenDTO.getToken(), "Token can not be null");
        log.info("Start signup confirm process:{}", actionRequestedByWithTokenDTO.getSubject());
        var user = this.userService.findByUsernameOrEmail(actionRequestedByWithTokenDTO.getSubject());
        if(user.isAccountActivated()) throw new AccountAlreadyActivatedException("Account already activated for email:" + actionRequestedByWithTokenDTO.getSubject());
        var token = this.tokenService.findTokenByUserAndTokenType(user, Token.TokenType.ACCOUNT_ACTIVATION);
        this.tokenService.compare(actionRequestedByWithTokenDTO.getToken(), token.getToken());
        this.tokenService.revokeAllUserTokensByTokenType(user, Token.TokenType.ACCOUNT_ACTIVATION);
        user.setAccountActivated(true);
        this.userService.save(user);
        log.info("Finish signup confirm process:{}", user.getEmail());
    }

    @Override
    public void passwordResetRequest(ActionRequestedByDTO actionRequestedByDTO) {
        Objects.requireNonNull(actionRequestedByDTO.getSubject(), "Email can not be null");
        log.info("Start executing password reset request process for email:{}", actionRequestedByDTO.getSubject());
        var user = this.userService.findByUsernameOrEmailAccountActivated(actionRequestedByDTO.getSubject());
        user.setPasswordResetRequested(true);
        this.userService.save(user);
        var token = this.tokenService.generateRandomToken();
        this.tokenService.save(new Token(token, Token.TokenType.PASSWORD_RESET, user));
        this.emailSenderPasswordResetConfirm.send(user, token);
        log.info("Finish executing password reset request process for email:{}", actionRequestedByDTO.getSubject());
    }

    @Override
    public void passwordResetTokenValidation(ActionRequestedByDTOWithTokenDTO actionRequestedByWithTokenDTO) {
        log.info("Start executing password reset token validation process for subject:{}", actionRequestedByWithTokenDTO.getSubject());
        var user = this.userService.findByUsernameOrEmailAccountActivated(actionRequestedByWithTokenDTO.getSubject());
        if(!user.isPasswordResetRequested()) throw new PasswordResetNeverRequestedException("subject: " + actionRequestedByWithTokenDTO.getSubject());
        var token = this.tokenService.findTokenByUserAndTokenType(user, Token.TokenType.PASSWORD_RESET);
        this.tokenService.compare(actionRequestedByWithTokenDTO.getToken(), token.getToken());
        log.info("Finish executing password reset token validation process for subject:{}", actionRequestedByWithTokenDTO.getSubject());
    }

    @Override
    public void passwordResetConfirm(PasswordChangeConfirmDTO passwordChangeConfirmDTO) {
        Objects.requireNonNull(passwordChangeConfirmDTO.getPassword(), "New password can not be null");
        log.info("Start executing password reset confirmation process for subject: {}", passwordChangeConfirmDTO.getSubject());
        var user = this.userService.findByUsernameOrEmailAccountActivated(passwordChangeConfirmDTO.getSubject());
        var token = this.tokenService.findTokenByUserAndTokenType(user, Token.TokenType.PASSWORD_RESET);
        this.tokenService.compare(passwordChangeConfirmDTO.getToken(), token.getToken());
        this.tokenService.revokeAllUserTokens(user);
        user.setPasswordResetRequested(false);
        user.setPassword(passwordEncoder.encode(passwordChangeConfirmDTO.getPassword()));
        this.userService.save(user);
        log.info("Finish executing password reset confirmation process for subject: {}", passwordChangeConfirmDTO.getSubject());
    }

    /**
     *  Este metodo se usa para autenticar al usuario recibiendo como input el username y password por ende este
     *  se ejecuta al momento del /login. Finalmente con una autenticacion existosa se setea el objeto Authentication que contiene
     *  el (principal: UserDetails y las granted authorities) al contexto de seguridad de spring (SecurityContext)
     *  Nota: el principal contiene UserDetails porque es lo que le retorna de ejecutar el metodo loadByUsername del UserDetailsService
     * @param username
     * @param password
     */
    private void authenticate(String username, String password) {
        log.info("New login from user {}, starting authentication process", username);
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken); // este método se encarga de autenticar al usuario utilizando el AuthenticationProvider correspondiente. Si la autenticación es exitosa, Spring Security se encargará automáticamente de establecer la autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(AuthenticationException e) {
            log.error(String.format("Authentication error for username: %s | %s",  username, e.getMessage()));
            throw new ApiException(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     *  Este metodo sirve para actualizar manualmente el contexto de seguridad (SecurityContext) con usuario haciendo el request actual.
     *  A este metodo lo vas a utilizar en tu JwtRequestFilter con request (request que sean posteriores al /login es decir cuando ya pasó previamente
     *  el proceso de autenticación) que contengan el token jwt valido. Por ejemplo si viene un request /catalogo y el request viene
     *  con un token jwt valido entonces quiere decir que el user se autenticó previamente correctamente por ende solo tenes que
     *  actualizar el contexto de seguridad de spring (SecurityContext) para guardar la data del usuario que esta queriendo realizar el request en cuestion.
     *  Fijate que el primer parametro de UsernamePasswordAuthenticationToken es de tipo Object es decir eso te permite
     *  pasarle cualquier cosa desde un username como en mi caso, un userId, o incluso un dto completo. La ventaja de pasarle mas
     *  info que un simple username o un userId es que con un dto por ejemplo despues la vas a tener disponible a esa informacion
     *  cuando en tu codigo accedas al securitycontext asi SecurityContextHolder.getContext().getAuthentication().getPrincipal()
     *  para retornar info del usuario logueado actual sin tener que ir a la base de datos para consultar la tabla de usuarios.
     *  Lo que si NO ES RECOMENDABLE es hacer una consulta a la db para traernos al usuario AppUser y guardalo en el UsernamePasswordAuthenticationToken
     *  porque esta query a la db se haría SIEMPRE en una y cada una de las request posteriores al /login para traer toda
     *  la info completa del AppUser y vos por ahi en tu logica del endpoint ni quiera te interesa la datos del user a nivel de db
     *  y entonces es al vicio imaginate un usuario hace 10 query a /catalogo y esto va a implicar hacer 10 consultas a la db a la tabla AppUser
     *  y ni si quiera te interesa la data almacenada en AppUser para ese endpoint.
     *  Entonces en resumen lo conveniente es guardar info del user sin ir a la db o guardas un username o id o dto.
     * @param username
     * @param grantedAuthorities
     */
    @Override
    public void updateSecurityContext(String username, Collection<GrantedAuthority> grantedAuthorities) {
        log.info("User already loggedin with credentials, so Authentication object will be updated because no need to authenticate with credentials again");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                username,
                null,
                grantedAuthorities // al pasarle como parametro las grantedAuthoritied en el contructor de UsernamePasswordAuthenticationToken fijate que inicializa isAuthenticated con valor true
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    @Override
    public JwtPairDTO renewAccessTokenPair(String refreshToken) {
        DecodedJWT decodedJWT = this.jwtService.verifyAndDecodeJwt(refreshToken, JwtScope.REFRESH_TOKEN);
        AppUser user = this.userService.findByUsernameOrEmailAccountActivated(decodedJWT.getSubject());
        String accessToken = this.jwtService.generateAccessToken(user);
        this.tokenService.save(new Token(accessToken, Token.TokenType.ACCESS_TOKEN, user));
        return new JwtPairDTO(accessToken, refreshToken);
    }

    @Override
    public JwtDTO renewAccessToken(String refreshToken) {
        DecodedJWT decodedJWT = this.jwtService.verifyAndDecodeJwt(refreshToken, JwtScope.REFRESH_TOKEN);
        AppUser user = this.userService.findByUsernameOrEmailAccountActivated(decodedJWT.getSubject());
        String accessToken = this.jwtService.generateAccessToken(user);
        this.tokenService.save(new Token(accessToken, Token.TokenType.ACCESS_TOKEN, user));
        return new JwtDTO(accessToken);
    }


}
