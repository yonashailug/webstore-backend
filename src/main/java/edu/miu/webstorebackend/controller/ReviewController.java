package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.domain.Review;
import edu.miu.webstorebackend.dto.FollowDto;
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import edu.miu.webstorebackend.service.review.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<Review> getAll() {
        return reviewService.findAll();
    }

    @PutMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    ResponseEntity<Review> approve(@PathVariable Long id) {
        Optional<Review> optional = reviewService.approve(id);
        if(optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.notFound().build();
    }
}
