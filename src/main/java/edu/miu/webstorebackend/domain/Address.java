package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Address {
    @Id
    private Long id;

    private String street;
    private String secondLine;
    private String city;
    private String state;
    private String zip;
}
