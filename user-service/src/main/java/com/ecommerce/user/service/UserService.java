package com.ecommerce.user.service;

import com.ecommerce.user.dto.PageResponse;
import com.ecommerce.user.dto.Response;
import com.ecommerce.user.dto.UserRequestDto;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;
    public Response register(UserRequestDto dto) {
        if(repository.existsByEmail(dto.getEmail()))
            return new Response(false, "EMAIL ALREADY EXISTS");
        repository.save(mapper.toUser(dto));
        return new Response(true, "USER SAVED");
    }

    public PageResponse getAllUsers(int page, int size, Boolean active, String sortField) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortField);
        Page<User> usersPage;
        if (active == null) {
            usersPage = repository.findAll(pageable);
        } else
            usersPage = repository.findAllByActive(active, pageable);
        return new PageResponse(
                mapper.toUserResponseDtoList(usersPage.getContent()),
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages()
        );
    }

    public Response getOneUser(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.map(user -> new Response(true, mapper.toUserResponseDto(user))).orElseGet(() -> new Response(false, "USER NOT FOUND"));
    }

    public Response deleteUserByAdmin(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        if(optionalUser.isEmpty())
            return new Response(false, "USER NOT FOUND");
        User user = optionalUser.get();
        user.setActive(false);
        repository.save(user);
        return new Response(true, "DELETED");
    }
}
