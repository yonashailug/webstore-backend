package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.domain.Follow;
import edu.miu.webstorebackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    @Query("SELECT f.seller from Follow f where f.buyer.id =:id")
    List<User> findSellersByBuyerId(@Param("id") Long id);
}
