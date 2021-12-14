package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

enum ReviewStatus {
    REQUESTED, APPROVED, REJECTED, REMOVED;
}

@Entity
@NoArgsConstructor
public class Review {
    @Id
    private Long id;

    private String title;
    private String description;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Buyer buyer;

    private ReviewStatus status;
    private LocalDateTime createdAt;
}
