package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Payment {
    @Id
    private Long id;

    @ManyToOne
    private Buyer buyer;

    private String name;
    private String cardNumber;
    private LocalDate expiration;
    private String securityCode;
    private double balance;
}
