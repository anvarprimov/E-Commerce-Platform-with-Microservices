package com.ecommerce.product.service;

import com.ecommerce.product.dto.*;
import com.ecommerce.product.entity.Product;
import com.ecommerce.product.mapper.ProductMapper;
import com.ecommerce.product.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper mapper;
    private static final Set<String> SORT_WHITELIST = Set.of("id","name","price","createdAt","updatedAt");

    public Response getALlProducts(ProductSearchRequest req) {
        if (req.minPrice() != null && req.maxPrice() != null && req.minPrice().compareTo(req.maxPrice()) > 0)
            return new Response(false, "minPrice cannot be greater than maxPrice");

        String sortField = req.sortOrDefault();
        if (!SORT_WHITELIST.contains(sortField)) sortField = "id";
        Sort sort = req.ascOrDefault() ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
        Pageable pageable = PageRequest.of(req.pageOrDefault(), req.sizeOrDefault(), sort);
        Specification<Product> spec = ProductSpecifications.filter(req);
        return new Response(true, toPageResponse(productRepository.findAll(spec, pageable)));
    }

    public Response getOne(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        return optionalProduct.map(product -> new Response(true, mapper.toProductResponseDto(product))).orElseGet(() -> new Response(false, "PRODUCT NOT FOUND"));
    }

    public Response create(ProductRequestDto dto) {
        Product product = new Product();
        updateProductHelper(product, dto);
        productRepository.save(product);
        return new Response(true, "SAVED");
    }

    public Response update(long id, ProductRequestDto dto) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            return new Response(false, "PRODUCT NOT FOUND");
        Product product = optionalProduct.get();
        updateProductHelper(product, dto);
        productRepository.save(product);
        return new Response(true, "SAVED");
    }

    public Response softDelete(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            return new Response(false, "PRODUCT NOT FOUND");
        Product product = optionalProduct.get();
        product.setActive(false);
        productRepository.save(product);
        return new Response(true, "SOFT DELETED");
    }

    public Response restore(long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isEmpty())
            return new Response(false, "PRODUCT NOT FOUND");
        Product product = optionalProduct.get();
        product.setActive(true);
        productRepository.save(product);
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

    public Response decreaseStock(StockRequestDto requestDto) {
        List<Product> productList = new ArrayList<>();
        for (StockUnitRequestDto dto : requestDto.getStockUnitRequestDtoList()) {
            Optional<Product> optionalProduct = productRepository.findById(dto.getProductId());
            if (optionalProduct.isEmpty())
                return new Response(false, dto.getProductId() + " PRODUCT NOT FOUND");
            Product product = optionalProduct.get();
            if (product.getQuantity() < dto.getQuantity())
                return new Response(false, dto.getProductId() + " product not enough available quantity: " + product.getQuantity());
            product.setQuantity(product.getQuantity() - dto.getQuantity());
            productList.add(product);
        }
        productRepository.saveAll(productList);
        return new Response(true, "SUCCESS");
    }
}
