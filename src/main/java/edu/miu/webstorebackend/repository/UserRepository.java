package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.model.ERole;
import edu.miu.webstorebackend.model.Role;
import edu.miu.webstorebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
     List<User> findAll();
     Optional<User> findByUsername(String userName);
     Boolean existsByEmail(String email);
     Boolean existsByUsername(String username);
     //boolean existsById(long id);
     Optional<User> findById(long id);


     @Transactional
     @Modifying
     @Query("UPDATE User u SET u.isEnabled = true where u.email = ?1")
     int enableUser(String email);

    public List<User> findAll();
    public Optional<User> findByUsername(String userName);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
    List<User> findUserByRolesIn(Set<Role> role);
}
