package edu.miu.webstorebackend.security.services.auth;

import edu.miu.webstorebackend.Utility.MailBuilder;
import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.dto.authDtos.requestdtos.RegistrationRequest;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.AuthenticationResponse;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.VerifyTokenResponse;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.RegistrationResponse;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.model.*;
import edu.miu.webstorebackend.security.jwt.JwtTokenUtil;
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import edu.miu.webstorebackend.service.EmailService.EmailService;
import edu.miu.webstorebackend.service.RefreshTokenService.RefreshTokenService;
import edu.miu.webstorebackend.service.RoleService.RoleService;
import edu.miu.webstorebackend.service.UserService.UserService;
import edu.miu.webstorebackend.service.VerificationTokenService.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService{

    private JwtTokenUtil jwtTokenUtil;
    private UserService userService;
    private RoleService roleService;
    private GenericMapper modelMapper;
    private RefreshTokenService refreshTokenService;
    private PasswordEncoder encoder;
    private VerificationTokenService verificationTokenService;
    private EmailService emailService;

    @Autowired
    public AuthServiceImpl(JwtTokenUtil jwtTokenUtil, UserService userService,
                           RoleService roleService, GenericMapper modelMapper,
                           RefreshTokenService refreshTokenService,
                           VerificationTokenService verificationTokenService,
                           EmailService emailService,
                           PasswordEncoder encoder) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userService = userService;
        this.roleService = roleService;
        this.modelMapper = modelMapper;
        this.refreshTokenService = refreshTokenService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
        this.encoder = encoder;
    }
    //Refactor it
    public AuthenticationResponse getCurrentUserRepresentation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String token = jwtTokenUtil.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList());
        UserDto userDto = (UserDto) modelMapper.mapObject(userDetails,UserDto.class);
        userDto.setRoles(roles);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());
        return new AuthenticationResponse(
                token,
                userDto,
                "Bearer",
                refreshToken.getToken()
        );
    }

    public AbstractMap.SimpleImmutableEntry<Boolean,RegistrationResponse> registerUser(
            RegistrationRequest registrationRequest, ERole role, boolean isEnabled) {
        if(checkUserExistsByEmail(registrationRequest.getEmail())) {
            return new AbstractMap.SimpleImmutableEntry<Boolean, RegistrationResponse>(
                    false,
                    new RegistrationResponse(" Email already exists!")
            );
        }
        if(checkUserExistsByUserName(registrationRequest.getUsername())) {
            return new AbstractMap.SimpleImmutableEntry<Boolean, RegistrationResponse>(
                    false,
                    new RegistrationResponse(" username is already taken!")
            );
        }

        User user = setUserDetails(registrationRequest, role, isEnabled);
        userService.addUser(user);
        String verificationToken = addVerificationToken(user);
        sendVerificationEmail(user.getUsername(), user.getEmail(), verificationToken);
        return new AbstractMap.SimpleImmutableEntry<Boolean, RegistrationResponse>(
                true,
                new RegistrationResponse( role + " Registered successfully. Check yor email, verification email has been sent to your email address.")
        );
    }

    public Optional<String> refreshAccessToken(String refreshToken) {
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyRefreshTokenExpiration)
                .map(RefreshToken::getUser)
                .map(user -> jwtTokenUtil.generateTokenFromUserName(user.getUsername()));

    }

    private boolean checkUserExistsByEmail(String email) {
        return userService.existsByEmail(email) ?true : false;
    }
    private boolean checkUserExistsByUserName(String username) {
        return userService.existsByUsername(username) ? true : false;
    }
    private User setUserDetails(RegistrationRequest registrationRequest, ERole userRole, boolean isEnabled) {
        Set<Role> roles = new HashSet<Role>();
        Role r = roleService.findByName(userRole);
        roles.add(r);
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setName(registrationRequest.getName());
        user.setPassword(encoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setEnabled(isEnabled);
        user.setRoles(roles);
        return user;
    }
    private String addVerificationToken(User user) {
        String verificationToken = UUID.randomUUID().toString();
        verificationTokenService.Save(user,verificationToken);
        return verificationToken;
    }

    private void sendVerificationEmail(String name, String email, String token) {
        try{
            String subject = "Confirm your Email";
            String link = "http://localhost:8888/api/auth/activate?token=" + token;
            String message = "Thank you for registering. Please click on the below link to activate your account.";
            emailService.sendEMail(email, subject,MailBuilder.buildConfirmationEmail(subject,name,message, link));
        }
        catch (MessagingException exception) {
            System.out.println(exception.getMessage());
        }
    }

    @Transactional
    public AbstractMap.SimpleImmutableEntry<Boolean,VerifyTokenResponse> verifyToken(String token) {
        VerificationToken verificationToken = verificationTokenService.findByToken(token);
        if(verificationToken == null) {
            return new AbstractMap.SimpleImmutableEntry<Boolean, VerifyTokenResponse> (
                    false,
                new VerifyTokenResponse("Unable to activate account, invalid activation link")
            );
        }
        ///FIXME --- add a mechanism if the user tries to verify using expired token
        if(verificationToken.getExpirationDate().isBefore(LocalDateTime.now()) ) {
            return new AbstractMap.SimpleImmutableEntry<Boolean, VerifyTokenResponse> (
                    false,
                    new VerifyTokenResponse("Unable to activate account, activation link expired")
            );
        }
        if(verificationToken.getConfirmationDate() != null) {
            return new AbstractMap.SimpleImmutableEntry<Boolean, VerifyTokenResponse> (
                    false,
                    new VerifyTokenResponse("Account is already active")
            );
        }
        verificationTokenService.setTokenConfirmationDate(token);
        List<ERole> userRoles = verificationToken.getUser().getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
        if( !userRoles.contains(ERole.SELLER)) {
            userService.enableUser(verificationToken.getUser().getEmail());
        }
        return new AbstractMap.SimpleImmutableEntry<Boolean, VerifyTokenResponse> (
                false,
                new VerifyTokenResponse("Account activated successfully")
        );

    }


}
