package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.dto.SellerDtos.ResponseDtos.ActivateSellerResponse;
import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.dto.authDtos.requestdtos.RegistrationRequest;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.RegistrationResponse;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import edu.miu.webstorebackend.security.services.auth.AuthService;
import edu.miu.webstorebackend.service.RoleService.RoleService;
import edu.miu.webstorebackend.service.UserManagementService.UserManagementService;
import edu.miu.webstorebackend.service.UserService.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user/regulate")
public class UserManagementController {

    private AuthService authService;
    private UserManagementService managementService;
    private RoleService roleService;
    private UserService userService;

    public UserManagementController(AuthService authService, UserManagementService userManagementService,
            RoleService roleService, UserService userService) {
        this.managementService = userManagementService;
        this.authService = authService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegistrationResponse> registerAdmin(@Valid @RequestBody RegistrationRequest registrationRequest) {
        var adminRegistrationEntry = authService.registerUser(registrationRequest, ERole.ADMIN, false);
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


    @GetMapping("/sellers")
    public ResponseEntity<List<UserDto>> getSellers() {
        Set<Role> roles = new HashSet<>();
        Role r = roleService.findByName(ERole.SELLER);
        roles.add(r);
        return ResponseEntity.ok().body(userService.findUserByRole(roles));
    }
}
