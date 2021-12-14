package edu.miu.webstorebackend.service.UserService;

import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import edu.miu.webstorebackend.model.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;


public interface UserService {

    public UserDto getById(long id);

    public UserDto findByUserName(String userName);

    public void addUser(User user);

    public void deleteById(long id);

    public void updateUser(long id, UserDto user);

    public Boolean existsByEmail(String email);

    public Boolean existsByUsername(String username);

    List<UserDto> findUserByRole(Set<Role> roles);
}
