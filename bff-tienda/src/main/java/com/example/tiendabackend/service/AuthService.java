package com.example.tiendabackend.service;

import com.example.tiendabackend.dto.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;

public interface AuthService {
    JwtPairDTO login(String username, String password);
    void signUp(SignUpDTO signUpDTO);
    void activateUserAccount(ActionRequestedByDTOWithTokenDTO actionRequestedByWithTokenDTO);
    void passwordResetRequest(ActionRequestedByDTO actionRequestedByDTO);
    void passwordResetTokenValidation(ActionRequestedByDTOWithTokenDTO actionRequestedByWithTokenDTO);
    void passwordResetConfirm(PasswordChangeConfirmDTO passwordChangeConfirmDTO);
    void updateSecurityContext(String username, Collection<GrantedAuthority> grantedAuthorities);
    JwtPairDTO renewAccessTokenPair(String refreshToken);
    JwtDTO renewAccessToken(String refreshToken);

    static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return  authentication != null && authentication.isAuthenticated();
    }
}
