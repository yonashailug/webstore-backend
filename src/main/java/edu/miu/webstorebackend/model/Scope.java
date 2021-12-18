package edu.miu.webstorebackend.model;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@NoArgsConstructor
public class Scope {
    @Id
    private Long id;

    private String name;
    private String uri;
}
