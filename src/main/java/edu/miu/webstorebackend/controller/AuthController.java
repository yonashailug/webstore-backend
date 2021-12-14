package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.dto.authDtos.requestdtos.AuthenticationRequest;
import edu.miu.webstorebackend.dto.authDtos.requestdtos.RegistrationRequest;
import edu.miu.webstorebackend.dto.authDtos.requestdtos.TokenRefreshRequest;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.AuthenticationResponse;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.RegistrationResponse;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.TokenRefreshResponse;
import edu.miu.webstorebackend.exception.TokenRefreshException;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.RefreshToken;
import edu.miu.webstorebackend.model.Role;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.security.jwt.JwtTokenUtil;
import edu.miu.webstorebackend.security.services.auth.AuthService;
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import edu.miu.webstorebackend.service.RefreshTokenService.RefreshTokenService;
import edu.miu.webstorebackend.service.RoleService.RoleService;
import edu.miu.webstorebackend.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "api/auth")

public class AuthController{
    private AuthenticationManager authenticationManager;
    private AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }
    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@Valid @RequestBody AuthenticationRequest authenticationRequest)  {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
            );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthenticationResponse response = authService.getCurrentUserRepresentation();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerBuyer(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var buyerRegistrationEntry = authService.registerUser(registrationRequest, ERole.BUYER, true);
        HttpStatus status = buyerRegistrationEntry.getKey() ? HttpStatus.OK : HttpStatus.FORBIDDEN;
        RegistrationResponse response = buyerRegistrationEntry.getValue();
        return new ResponseEntity(response, status);
    }
    @PostMapping("/seller/register")
    public ResponseEntity<RegistrationResponse> registerSeller(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var sellerRegistrationEntry = authService.registerUser(registrationRequest, ERole.SELLER, false);
        HttpStatus status = sellerRegistrationEntry.getKey() ? HttpStatus.OK : HttpStatus.FORBIDDEN;
        RegistrationResponse response = sellerRegistrationEntry.getValue();
        return new ResponseEntity(response, status);
    }

    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        String refreshToken = tokenRefreshRequest.getRefreshToken();
        String token = authService.refreshAccessToken(refreshToken).orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh Token is not in database"));
        return ResponseEntity.ok(new TokenRefreshResponse(
                    token,
                    refreshToken,
                "Bearer"
                    ));
    }


}
