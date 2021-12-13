package edu.miu.webstorebackend.service.UserService;

import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.dto.UserDto;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private GenericMapper mapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, GenericMapper mapper) {
        this.userRepository = userRepository;
        this.mapper = mapper;
    }

    @Override
    public UserDto getById(long id) {
        return (UserDto) mapper.mapObject(userRepository.findById(id).get().getClass(),UserDto.class);
    }

    @Override
    public UserDto findByUserName(String userName) {
        User user = userRepository.findByUsername(userName).orElse(null);
        return (UserDto) mapper.mapObject(user, UserDto.class);
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void updateUser(long id, UserDto userDto) {
        User user = userRepository.findById(id).orElse(null);
        if(user != null) {
            user.setName(userDto.getName());
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            userRepository.save(user);
        }
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
}
