package com.ecommerce.product.service;

import com.ecommerce.product.dto.PageResponse;
import com.ecommerce.product.dto.ProductRequestDto;
import com.ecommerce.product.dto.Response;
import com.ecommerce.product.dto.StockRequestDto;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    public Response getOne(long id) {
        Optional<Product> optionalProduct = repository.findById(id);
        return optionalProduct.map(product -> new Response(true, mapper.toProductResponseDto(product))).orElseGet(() -> new Response(false, "PRODUCT NOT FOUND"));
    }

    public PageResponse search(String key, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage;
        if(key == null || key.isBlank())
            productPage = repository.findAll(pageable);
        else
            productPage = repository.findAllByNameContainingIgnoreCase(key.toLowerCase(), pageable);
        return toPageResponse(productPage);
    }

    public Response create(ProductRequestDto dto) {
        Product product = new Product();
        updateProductHelper(product, dto);
        repository.save(product);
        return new Response(true, "SAVED");
    }

    public Response update(long id, ProductRequestDto dto) {
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isEmpty())
            return new Response(false, "PRODUCT NOT FOUND");
        Product product = optionalProduct.get();
        updateProductHelper(product, dto);
        repository.save(product);
        return new Response(true, "SAVED");
    }

    public Response softDelete(long id) {
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isEmpty())
            return new Response(false, "PRODUCT NOT FOUND");
        Product product = optionalProduct.get();
        product.setActive(false);
        repository.save(product);
        return new Response(true, "SOFT DELETED");
    }

    public Response restore(long id) {
        Optional<Product> optionalProduct = repository.findById(id);
        if (optionalProduct.isEmpty())
            return new Response(false, "PRODUCT NOT FOUND");
        Product product = optionalProduct.get();
        product.setActive(true);
        repository.save(product);
        return new Response(true, "RESTORED");
    }

    private void updateProductHelper(Product product, ProductRequestDto dto) {
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setBrand(dto.getBrand());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
    }

    private PageResponse toPageResponse(Page<Product> productPage) {
        return new PageResponse(
                mapper.toProductResponseDtoList(productPage.getContent()),
                productPage.getNumber(),
                productPage.getSize(),
                productPage.getTotalElements(),
                productPage.getTotalPages()
        );
    }
// todo
    public Response decreaseStock(StockRequestDto requestDto) {
//        Optional<Product> optionalProduct = repository.findById(id);
//        if (optionalProduct.isEmpty())
//            return new Response(false, "PRODUCT NOT FOUND");
//        Product product = optionalProduct.get();
////        if (product.getQuantity() < quantity)
        return null;
    }
}
