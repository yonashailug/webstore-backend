package edu.miu.webstorebackend.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Rating {
    @Id
    private Long id;

    @ManyToOne
    private Buyer buyer;

    @ManyToOne
    private Product product;
    private int star;
    private LocalDateTime createdAt;
}
