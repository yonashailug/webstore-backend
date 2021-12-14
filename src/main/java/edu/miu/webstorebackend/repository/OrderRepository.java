package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    @Query("SELECT o FROM Order o where o.user.id = :id")
    List<Order> findOrdersByUserId(@Param("id") Long id);
}
