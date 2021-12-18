package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
