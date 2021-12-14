package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.dto.authDtos.requestdtos.RegistrationRequest;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.RegistrationResponse;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.security.services.auth.AuthService;
import edu.miu.webstorebackend.service.RefreshTokenService.RefreshTokenService;
import edu.miu.webstorebackend.service.RoleService.RoleService;
import edu.miu.webstorebackend.service.UserService.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/user/regulate")
public class UserManagementController {

    private AuthService authService;

    public UserManagementController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegistrationResponse> registerAdmin(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var sellerRegistrationEntry = authService.registerUser(registrationRequest, ERole.SELLER, false);
        HttpStatus status = sellerRegistrationEntry.getKey() ? HttpStatus.OK : HttpStatus.FORBIDDEN;
        RegistrationResponse response = sellerRegistrationEntry.getValue();
        return new ResponseEntity(response, status);
    }
}
