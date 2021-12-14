package edu.miu.webstorebackend.service.follow;

import edu.miu.webstorebackend.dto.FollowDto;
import edu.miu.webstorebackend.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface FollowService {
    List<UserDto> getFollowing(Long buyerId);
    Optional<FollowDto> getById(Long id);
    Optional<FollowDto> save(FollowDto followDto);
    Optional<FollowDto> delete(Long id);
}
