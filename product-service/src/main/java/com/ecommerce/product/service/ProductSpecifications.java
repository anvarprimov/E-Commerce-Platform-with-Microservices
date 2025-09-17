package com.ecommerce.product.service;

import com.ecommerce.product.dto.ProductSearchRequest;
import com.ecommerce.product.entity.Product;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public final class ProductSpecifications {

    public static Specification<Product> filter(ProductSearchRequest req) {
        return Specification.allOf(
                textSearch(req.search()),
                priceGte(req.minPrice()),
                priceLte(req.maxPrice()),
                active(req.activeOrDefault()));
    }

    private static Specification<Product> textSearch(String q) {
        if (q == null || q.isBlank()) return null;
        String like = "%" + q.trim().toLowerCase() + "%";
        return (root, query, cb) -> cb.or(
                cb.like(cb.lower(root.get("name")), like),
                cb.like(cb.lower(root.get("description")), like),
                cb.like(cb.lower(root.get("brand")), like)
        );
    }
    private static Specification<Product> priceGte(BigDecimal min) {
        if (min == null) return null;
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("price"), min);
    }
    private static Specification<Product> priceLte(BigDecimal max) {
        if (max == null) return null;
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("price"), max);
    }
    private static Specification<Product> active(Boolean isActive) {
        if (isActive == null) return null;
        return (root, query, cb) -> cb.equal(root.get("active"), isActive);
    }
}
