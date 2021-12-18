package edu.miu.webstorebackend.security.services.auth;

import edu.miu.webstorebackend.dto.authDtos.requestdtos.RegistrationRequest;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.AuthenticationResponse;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.VerifyTokenResponse;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.RegistrationResponse;
import edu.miu.webstorebackend.model.ERole;

import java.util.AbstractMap;
import java.util.Optional;

public interface AuthService {
    Optional<String> refreshAccessToken(String refreshToken);
    AuthenticationResponse getCurrentUserRepresentation();
    AbstractMap.SimpleImmutableEntry<Boolean, RegistrationResponse> registerUser(
            RegistrationRequest registrationRequest, ERole role, boolean isEnabled);
    AbstractMap.SimpleImmutableEntry<Boolean, VerifyTokenResponse> verifyToken(String token);
}
