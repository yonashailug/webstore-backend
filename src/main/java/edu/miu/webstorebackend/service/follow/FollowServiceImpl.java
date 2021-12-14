package edu.miu.webstorebackend.service.follow;

import edu.miu.webstorebackend.domain.Follow;
import edu.miu.webstorebackend.domain.Follow;
import edu.miu.webstorebackend.domain.Follow;
import edu.miu.webstorebackend.dto.FollowDto;
import edu.miu.webstorebackend.dto.FollowDto;
import edu.miu.webstorebackend.dto.FollowDto;
import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.repository.FollowRepository;
import edu.miu.webstorebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FollowServiceImpl implements FollowService{
    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final GenericMapper mapper;



    @Override
    public List<UserDto> getFollowing(Long buyerId) {
        List<User> following = followRepository.findSellersByBuyerId(buyerId);
        return mapper.mapList(following, UserDto.class);
    }

    @Override
    public Optional<FollowDto> getById(Long id) {
        Optional<Follow> optionalFollow = followRepository.findById(id);
        if (optionalFollow.isPresent()) {
            FollowDto dto = toFollowDto(optionalFollow.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<FollowDto> save(FollowDto followDto) {
        Follow follow = fromFollowDto(followDto);
        followRepository.save(follow);
        FollowDto dto = toFollowDto(follow);
        return Optional.of(dto);
    }

    @Override
    public Optional<FollowDto> delete(Long id) {
        Optional<Follow> optionalFollow = followRepository.findById(id);
        if (optionalFollow.isPresent()) {
            followRepository.deleteById(id);
            FollowDto dto = toFollowDto(optionalFollow.get());
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    private FollowDto toFollowDto(Follow follow) {
        FollowDto dto = new FollowDto();
        dto.setId(follow.getId());
        dto.setSellerId(follow.getSeller().getId());
        dto.setBuyerId(follow.getBuyer().getId());
        dto.setDate(follow.getDate());
        return dto;
    }

    private Follow fromFollowDto(FollowDto dto) {
        Follow follow = new Follow();
        follow.setDate(LocalDateTime.now());
        follow.setBuyer(userRepository.getById(dto.getBuyerId()));
        follow.setSeller(userRepository.getById(dto.getSellerId()));
        return follow;
    }
}
