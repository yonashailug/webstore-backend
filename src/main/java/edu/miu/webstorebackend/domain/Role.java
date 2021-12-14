package edu.miu.webstorebackend.domain;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
@NoArgsConstructor
public class Role {
    @Id
    private Long id;

    private String name;

    @OneToMany
    private List<Scope> scopes;
}
