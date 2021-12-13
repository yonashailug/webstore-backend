package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(ERole role);
}
