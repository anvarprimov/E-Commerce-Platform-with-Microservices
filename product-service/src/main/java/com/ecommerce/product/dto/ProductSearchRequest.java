package com.ecommerce.product.dto;

import java.math.BigDecimal;

public record ProductSearchRequest(
        Integer page,
        Integer size,
        String search,
        BigDecimal minPrice,
        BigDecimal maxPrice,
        Boolean active,
        String sort,
        Boolean asc
) {
    public int pageOrDefault()  { return page  == null ? 0  : Math.max(page, 0); }
    public int sizeOrDefault()  { return size  == null ? 20 : Math.min(Math.max(size, 1), 200); }
    public String sortOrDefault(){ return (sort == null || sort.isBlank()) ? "id" : sort; }
    public boolean ascOrDefault(){ return asc == null || asc; }
    public Boolean activeOrDefault(){ return active == null || active; }
}
