package edu.miu.webstorebackend.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String title;
    private String description;

//    @ManyToOne
//    private Product product;

    @ManyToOne
    private User buyer;

    private ReviewStatus status;
    private LocalDateTime createdAt;
}
