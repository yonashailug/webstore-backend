package edu.miu.webstorebackend.service.RoleService;

import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import edu.miu.webstorebackend.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService{

    private RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Role findByName(ERole role) {
        return roleRepository.findByName(role).orElse(null);
    }
}
