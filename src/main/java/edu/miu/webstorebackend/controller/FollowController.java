package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.dto.FollowDto;
import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.security.services.UserDetailsImpl;
import edu.miu.webstorebackend.service.follow.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/following")
public class FollowController {
    private final FollowService followService;

    @GetMapping
    @PreAuthorize("hasRole('Buyer')")
    ResponseEntity<List<UserDto>> getFollowing() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        List<UserDto> dtos = followService.getFollowing(userId);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("{id}")
    ResponseEntity<FollowDto> getById(@RequestParam Long id) {
        Optional<FollowDto> optional = followService.getById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    ResponseEntity<FollowDto> follow(@RequestBody FollowDto followDto) {
        Optional<FollowDto> optional = followService.save(followDto);
        if (optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("{id}")
    ResponseEntity<FollowDto> unfollow(@PathVariable Long id) {
        Optional<FollowDto> optional = followService.delete(id);
        if(optional.isPresent()) {
            return ResponseEntity.ok(optional.get());
        }
        return ResponseEntity.notFound().build();
    }
}
