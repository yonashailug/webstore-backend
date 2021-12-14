package edu.miu.webstorebackend.domain;

import edu.miu.webstorebackend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    private Long id;

    @ManyToOne
    private User buyer;

    @ManyToOne
    private User seller;

    private LocalDateTime date;
}
