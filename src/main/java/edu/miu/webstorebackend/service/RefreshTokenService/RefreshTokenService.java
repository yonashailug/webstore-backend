package edu.miu.webstorebackend.service.RefreshTokenService;

import edu.miu.webstorebackend.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
     Optional<RefreshToken> findByToken(String token);
     RefreshToken createRefreshToken(Long userId);
     RefreshToken verifyRefreshTokenExpiration(RefreshToken refreshToken);
     int deleteTokenByUserId(Long id);
}
