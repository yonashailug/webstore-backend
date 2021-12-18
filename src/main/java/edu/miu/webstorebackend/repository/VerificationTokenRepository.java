package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.model.VerificationToken;
import org.hibernate.sql.Update;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    Optional<VerificationToken> findByToken(String token);
    @Transactional
    @Modifying
    @Query("UPDATE VerificationToken c SET c.confirmationDate = ?2 where c.token = ?1")
    int updateConfirmationDate(String toke, LocalDateTime confirmationDate);
}
