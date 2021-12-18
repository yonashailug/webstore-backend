package edu.miu.webstorebackend.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Coupon {
    @Id
    private Long id;

    private String name;
    private double discount;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}
