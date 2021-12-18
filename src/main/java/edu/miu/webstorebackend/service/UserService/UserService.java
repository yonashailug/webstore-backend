package edu.miu.webstorebackend.service.UserService;

import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.model.User;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;



public interface UserService {

     User getById(long id);

     UserDto findByUserName(String userName);

     void addUser(User user);

     void deleteById(long id);

     void updateUser(long id, UserDto user);

     Boolean existsByEmail(String email);

     Boolean existsByUsername(String username);
     boolean existsById(long id);

     int enableUser(String email);
}
