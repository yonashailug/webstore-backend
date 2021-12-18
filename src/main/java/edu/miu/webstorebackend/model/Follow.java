package edu.miu.webstorebackend.model;

import edu.miu.webstorebackend.model.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User buyer;

    @ManyToOne
    private User seller;

    private LocalDateTime date;
}
