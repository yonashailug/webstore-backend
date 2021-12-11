package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Admin extends Role{
    @Id
    private Long id;
}
