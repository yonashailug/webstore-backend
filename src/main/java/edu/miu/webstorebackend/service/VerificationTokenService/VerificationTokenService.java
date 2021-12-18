package edu.miu.webstorebackend.service.VerificationTokenService;

import edu.miu.webstorebackend.dto.authDtos.responsedtos.VerifyTokenResponse;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.model.VerificationToken;

public interface VerificationTokenService {
    VerificationToken findByToken(String token);
    void Save(User user, String token);
     int setTokenConfirmationDate(String token);
}
