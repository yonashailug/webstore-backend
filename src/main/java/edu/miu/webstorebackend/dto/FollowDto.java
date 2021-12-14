package edu.miu.webstorebackend.dto;

import edu.miu.webstorebackend.model.User;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class FollowDto {
    private Long id;
    private Long buyerId;
    private Long sellerId;
    private LocalDateTime date;
}
