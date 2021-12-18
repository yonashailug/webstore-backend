package edu.miu.webstorebackend.service.UserManagementService;

import edu.miu.webstorebackend.dto.SellerDtos.ResponseDtos.ActivateSellerResponse;
import edu.miu.webstorebackend.dto.authDtos.responsedtos.VerifyTokenResponse;
import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.AbstractMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserManagementServiceImpl implements UserManagementService{

    private UserService userService;


    @Autowired
    public UserManagementServiceImpl(UserService userService) {
        this.userService = userService;
    }

    public AbstractMap.SimpleImmutableEntry<Boolean, ActivateSellerResponse> activateSeller(long id) {
        if(!userService.existsById(id)) {
            return new AbstractMap.SimpleImmutableEntry<>(
                    false,
                    new ActivateSellerResponse("user not found")
            );
        }
        List<ERole> roles = userService.getById(id).getRoles().stream().map(role -> role.getName()).collect(Collectors.toList());
        if(!roles.contains(ERole.SELLER)) {
            return new AbstractMap.SimpleImmutableEntry<>(
                    false,
                    new ActivateSellerResponse("User is not a seller")
            );
        }
        userService.enableUser(userService.getById(id).getEmail());
        return new AbstractMap.SimpleImmutableEntry<>(
                true,
                new ActivateSellerResponse("seller approved")
        );
    }
}
