package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.dto.SellerDtos.ResponseDtos.ActivateSellerResponse;
import edu.miu.webstorebackend.dto.authDtos.requestdtos.RegistrationRequest;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.RegistrationResponse;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.security.services.auth.AuthService;
import edu.miu.webstorebackend.service.UserManagementService.UserManagementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/user/regulate")
public class UserManagementController {

    private AuthService authService;
    private UserManagementService managementService;

    public UserManagementController(AuthService authService, UserManagementService userManagementService) {
        this.managementService = userManagementService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegistrationResponse> registerAdmin(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var adminRegistrationEntry = authService.registerUser(registrationRequest, ERole.SELLER, false);
        HttpStatus status = adminRegistrationEntry.getKey() ? HttpStatus.OK : HttpStatus.FORBIDDEN;
        RegistrationResponse response = adminRegistrationEntry.getValue();
        return new ResponseEntity(response, status);
    }
    @PostMapping("/approve/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ActivateSellerResponse> activateSeller(@PathVariable long id) {
        var sellerActivationEntry =  managementService.activateSeller(id);
        HttpStatus status = sellerActivationEntry.getKey() ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        ActivateSellerResponse activateSellerResponse = sellerActivationEntry.getValue();
        return new ResponseEntity<>(activateSellerResponse, status);
    }

    
}
