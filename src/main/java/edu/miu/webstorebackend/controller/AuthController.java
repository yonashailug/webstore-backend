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
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import edu.miu.webstorebackend.service.RefreshTokenService.RefreshTokenService;
import edu.miu.webstorebackend.service.RoleService.RoleService;
import edu.miu.webstorebackend.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "api/auth")

public class AuthController{
    private AuthenticationManager authenticationManager;
    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    private RoleService roleService;
    private GenericMapper modelMapper;
    private RefreshTokenService refreshTokenService;
    private PasswordEncoder encoder;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          JwtTokenUtil jwtTokenUtil,
                          UserService userService,
                          RoleService roleService,
                          RefreshTokenService refreshTokenService,
                          PasswordEncoder encoder,
                          GenericMapper modelMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.roleService = roleService;
        this.refreshTokenService = refreshTokenService;
        this.encoder = encoder;
        this.modelMapper = modelMapper;
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
        String token = jwtTokenUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        UserDto userDto = (UserDto) modelMapper.mapObject(userDetails,UserDto.class);
        userDto.setRoles(roles);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return ResponseEntity.ok(
                    new AuthenticationResponse(
                        token,
                        userDto,
                        "Bearer",
                            refreshToken.getToken()
                    )
                );
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerBuyer(@Valid @RequestBody RegistrationRequest registrationRequest) {
        if(userService.existsByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Username is already taken"));
        }
        if(userService.existsByEmail(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("The email is already associated with an account"));
        }
        Set<Role> roles = new HashSet<Role>();
        Role r = roleService.findByName(ERole.BUYER);
        roles.add(r);
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setName(registrationRequest.getName());
        user.setPassword(encoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setEnabled(true);
        user.setRoles(roles);
        userService.addUser(user);
        return ResponseEntity.ok().body(new RegistrationResponse("User Registered Successfully"));
    }
    @PostMapping("/seller/register")
    public ResponseEntity<RegistrationResponse> registerSeller(@Valid @RequestBody RegistrationRequest registrationRequest) {
        if(userService.existsByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Username is already taken"));
        }
        if(userService.existsByEmail(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("The email is already associated with an account"));
        }
        Set<Role> roles = new HashSet<Role>();
        Role r = roleService.findByName(ERole.SELLER);
        roles.add(r);
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setName(registrationRequest.getName());
        user.setPassword(encoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setEnabled(false);
        user.setRoles(roles);
        userService.addUser(user);
        return ResponseEntity.ok().body(new RegistrationResponse("Seller Registered Successfully"));
    }



    @PostMapping("/token/refresh")
    public ResponseEntity<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest tokenRefreshRequest) {
        String refreshToken = tokenRefreshRequest.getRefreshToken();
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyRefreshTokenExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtTokenUtil.generateTokenFromUserName(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(
                            token,
                            refreshToken,
                            "Bearer"
                    ));
                })
                .orElseThrow(() -> new TokenRefreshException(refreshToken, "Refresh Token is not in database"));
    }


}
