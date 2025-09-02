package com.ecommerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse {
    private List<UserResponseDto> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
}
