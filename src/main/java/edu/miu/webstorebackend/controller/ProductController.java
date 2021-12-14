package edu.miu.webstorebackend.controller;

import edu.miu.webstorebackend.dto.ProductDto;
import edu.miu.webstorebackend.security.services.spring.UserDetailsImpl;
import edu.miu.webstorebackend.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/buyer")
    ResponseEntity<List<ProductDto>> getProductsByBuyer() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        List<ProductDto> orderDtos = productService.getProductsByBuyer(userId);
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/seller")
    ResponseEntity<List<ProductDto>> getProductsBySeller() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        List<ProductDto> orderDtos = productService.getProductsBySeller(userId);
        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if(productService.isProductBelongToUser(id, userId)) {
            Optional<ProductDto> optionalProductDto = productService.getProduct(id);
            if (optionalProductDto.isPresent()) {
                return ResponseEntity.ok(optionalProductDto.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping
    ResponseEntity<ProductDto> create(@RequestBody ProductDto productDto) {
        Optional<ProductDto> optionalProductDto = productService.save(productDto);
        if (optionalProductDto.isPresent()) {
            return ResponseEntity.ok(optionalProductDto.get());
        }
        else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("{id}")
    ResponseEntity<ProductDto> update(@RequestBody ProductDto productDto, @RequestParam Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if(productService.isProductBelongToUser(id, userId)) {
            Optional<ProductDto> optionalProductDto = productService.update(id, productDto);
            if (optionalProductDto.isPresent()) {
                return ResponseEntity.ok(optionalProductDto.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        }
        else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @DeleteMapping("{id}")
    ResponseEntity<ProductDto> delete(@RequestParam Long id) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        if (productService.isProductBelongToUser(id, userId)) {
            Optional<ProductDto> optionalProductDto = productService.delete(id);
            if (optionalProductDto.isPresent()) {
                return ResponseEntity.ok(optionalProductDto.get());
            } else {
                return ResponseEntity.badRequest().build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping()
    ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok(productService.findAllProducts());
    }
}
