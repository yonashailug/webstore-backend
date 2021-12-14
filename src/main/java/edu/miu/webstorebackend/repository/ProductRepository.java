package edu.miu.webstorebackend.repository;

import edu.miu.webstorebackend.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("select p from Product p where p.seller.id = :id")
    List<Product> findBySellerId(@Param("id") Long id);

    @Query("SELECT p from Product p where p.seller.id IN :following")
    List<Product> findByFollower(@Param("following") List<Long> following);
}
