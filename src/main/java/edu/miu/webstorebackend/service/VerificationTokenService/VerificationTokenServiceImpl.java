package edu.miu.webstorebackend.service.VerificationTokenService;

import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.model.VerificationToken;
import edu.miu.webstorebackend.repository.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{
    private VerificationTokenRepository verificationTokenRepository;
    @Value("${webstore.app.VerificationTokenExpirationHours}")
    private int verificationTokenExpirationHours;
    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }

    public VerificationToken findByToken(String token) {
        return verificationTokenRepository.findByToken(token).orElse(null);
    }

    public void Save(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusHours(verificationTokenExpirationHours),
                user);
        verificationTokenRepository.save(verificationToken);
    }

    public int setTokenConfirmationDate(String token) {
        return verificationTokenRepository.updateConfirmationDate(token, LocalDateTime.now());
    }

}
