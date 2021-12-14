package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.dto.authDtos.requestdtos.RegistrationRequest;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.RegistrationResponse;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.service.RefreshTokenService.RefreshTokenService;
import edu.miu.webstorebackend.service.RoleService.RoleService;
import edu.miu.webstorebackend.service.UserService.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/user/regulate")
public class UserManagementController {

    private PasswordEncoder encoder;
    private UserService userService;
    private RoleService roleService;

    public UserManagementController(UserService userService,
                                    RoleService roleService,
                                    PasswordEncoder encoder) {
        this.encoder = encoder;
        this.userService = userService;
        this.roleService = roleService;
    }


    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RegistrationResponse> registerAdmin(@Valid @RequestBody RegistrationRequest registrationRequest) {
        if(userService.existsByUsername(registrationRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("Username is already taken"));
        }
        if(userService.existsByEmail(registrationRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse("The email is already associated with an account"));
        }
        Set<Role> roles = new HashSet<Role>();
        Role r = roleService.findByName(ERole.ADMIN);
        roles.add(r);
        User user = new User();
        user.setUsername(registrationRequest.getUsername());
        user.setName(registrationRequest.getName());
        user.setPassword(encoder.encode(registrationRequest.getPassword()));
        user.setEmail(registrationRequest.getEmail());
        user.setEnabled(true);
        user.setRoles(roles);
        userService.addUser(user);

        return ResponseEntity.ok().body(new RegistrationResponse("Admin Registered Successfully"));
    }

    @GetMapping("/sellers")
    public ResponseEntity<List<UserDto>> getSellers() {
        Set<Role> roles = new HashSet<>();
        Role r = roleService.findByName(ERole.SELLER);
        roles.add(r);
        return ResponseEntity.ok().body(userService.findUserByRole(roles));
    }
}
