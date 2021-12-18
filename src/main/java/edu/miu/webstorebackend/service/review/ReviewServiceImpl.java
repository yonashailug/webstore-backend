package edu.miu.webstorebackend.service.review;

import edu.miu.webstorebackend.model.Review;
import edu.miu.webstorebackend.model.ReviewStatus;
import edu.miu.webstorebackend.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;


    @Override
    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> approve(Long id) {
        Optional<Review> reviewOptional = reviewRepository.findById(id);
        if (reviewOptional.isPresent()) {
            Review review = reviewOptional.get();
            review.setStatus(ReviewStatus.APPROVED);
            return Optional.of(reviewRepository.save(review));
        }
        return Optional.empty();
    }
}
