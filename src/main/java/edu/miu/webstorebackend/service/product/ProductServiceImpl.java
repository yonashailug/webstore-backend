package edu.miu.webstorebackend.service.product;

import edu.miu.webstorebackend.domain.Product;
import edu.miu.webstorebackend.dto.ProductDto;
import edu.miu.webstorebackend.helper.GenericMapper;
import edu.miu.webstorebackend.model.User;
import edu.miu.webstorebackend.repository.FollowRepository;
import edu.miu.webstorebackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final FollowRepository followRepository;

    private final GenericMapper mapper;

    @Override
    public List<ProductDto> getProductsByBuyer(Long id) {
        List<User> users = followRepository.findSellersByBuyerId(id);
        List<Long> following = users.stream().map(user -> user.getId()).collect(Collectors.toList());
        List<Product> products = productRepository.findByFollower(following);
        return mapper.mapList(products, ProductDto.class);
    }

    @Override
    public List<ProductDto> getProductsBySeller(Long id) {
        List<Product> products = productRepository.findBySellerId(id);
        return mapper.mapList(products, ProductDto.class);
    }

    @Override
    public boolean isProductBelongToUser(Long productId, Long userId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if(optionalProduct.isPresent()) {
            if(optionalProduct.get().getSeller().getId() == userId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Optional<ProductDto> getProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            ProductDto dto = (ProductDto)mapper.mapObject(optionalProduct.get(), ProductDto.class);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ProductDto> save(ProductDto productDto) {
        Product product = (Product) mapper.mapObject(productDto, Product.class);
        productRepository.save(product);
        ProductDto dto = (ProductDto)mapper.mapObject(product, ProductDto.class);
        return Optional.of(dto);
    }

    @Override
    public Optional<ProductDto> update(Long id, ProductDto productDto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productDto.setId(id);
            Product product = optionalProduct.get();
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
            modelMapper.map(productDto, product);
            productRepository.save(product);
            ProductDto dto = (ProductDto)mapper.mapObject(product, ProductDto.class);
            return Optional.of(dto);
        }
        return Optional.empty();
    }

    @Override
    public Optional<ProductDto> delete(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            productRepository.deleteById(id);
            ProductDto dto = (ProductDto)mapper.mapObject(optionalProduct.get(), ProductDto.class);
            return Optional.of(dto);
        }
        return Optional.empty();
    }
}