package edu.miu.webstorebackend.service.product;

import edu.miu.webstorebackend.domain.Product;
import edu.miu.webstorebackend.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface ProductService {
    List<ProductDto> getProductsByBuyer(Long id);
    List<ProductDto> getProductsBySeller(Long id);

    boolean isProductBelongToUser(Long productId, Long userId);
    Optional<ProductDto> getProduct(Long id);
    Optional<ProductDto> save(ProductDto productDto);
    Optional<ProductDto> update(Long id, ProductDto productDto);
    Optional<ProductDto> delete(Long id);

    List<ProductDto> findAllProducts();
}
