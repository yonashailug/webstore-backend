package edu.miu.webstorebackend.service.RoleService;

import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import org.springframework.stereotype.Service;

import java.util.Optional;

public interface RoleService {
    public Role findByName(ERole role);
}
