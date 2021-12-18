package edu.miu.webstorebackend.service.review;

import edu.miu.webstorebackend.domain.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    List<Review> findAll();
    Optional<Review> approve(Long id);
}
