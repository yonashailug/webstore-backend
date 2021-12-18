package edu.miu.webstorebackend.model;

import edu.miu.webstorebackend.model.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User buyer;

    private String name;
    private String cardNumber;
    private LocalDate expiration;
    private String securityCode;
    private double balance;
}
