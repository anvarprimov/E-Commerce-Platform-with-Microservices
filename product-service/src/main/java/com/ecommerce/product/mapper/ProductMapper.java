package com.ecommerce.product.mapper;

import com.ecommerce.product.dto.ProductResponseDto;
import com.ecommerce.product.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductResponseDto toProductResponseDto(Product product);
    List<ProductResponseDto> toProductResponseDtoList(List<Product> productList);
}
