package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
