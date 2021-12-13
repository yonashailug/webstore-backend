package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public List<User> findAll();
    public Optional<User> findByUsername(String userName);
    Boolean existsByEmail(String email);
    Boolean existsByUsername(String username);
}
