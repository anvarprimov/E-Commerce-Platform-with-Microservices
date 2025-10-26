package com.ecommerce.user.service;

import com.ecommerce.user.dto.*;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final KeycloakAdminService keycloakAdminService;
    private final UserMapper mapper;
    public Response<Object> register(UserRequestDto dto) {
        String role = "USER";
        return addUserWithRole(dto, role);
    }

    public Response<Object> add(UserRequestDto dto) {
        String role = "ADMIN";
        return addUserWithRole(dto, role);
    }

    @Transactional
    public Response<Object> addUserWithRole(UserRequestDto dto, String role) {
        if (repository.existsByEmail(dto.getEmail()))
            return Response.fail("EMAIL ALREADY EXISTS");


        try {
            String token = keycloakAdminService.getAdminAccessToken();

            String keycloakId = keycloakAdminService.createUser(token, dto);

            boolean ok = keycloakAdminService.assignRealmRoleToUser(token, keycloakId, role);
            if (!ok) {
                return Response.fail("KEYCLOAK ERROR: COULD NOT ASSIGN ROLE '" + role + "'");
            }

            User user = mapper.toUser(dto);
            user.setKeycloakId(keycloakId);
            repository.save(user);

            return Response.ok();

        } catch (Exception e) {
            e.printStackTrace();
            return Response.fail("REGISTRATION FAILED: " + e.getMessage());
        }
    }

    public PageResponse getAllUsers(int page, int size, Boolean active, String sortField) {
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.ASC, sortField);
        Page<User> usersPage = repository.findAllByActive(active, pageable);
        return new PageResponse(
                mapper.toUserResponseDtoList(usersPage.getContent()),
                usersPage.getNumber(),
                usersPage.getSize(),
                usersPage.getTotalElements(),
                usersPage.getTotalPages()
        );
    }

    public Response<UserResponseDto> getOneUser(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        return optionalUser.map(user -> Response.okData(mapper.toUserResponseDto(user))).orElseGet(() -> Response.fail("USER NOT FOUND"));
    }

    public Response<Object> deactivate(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        if(optionalUser.isEmpty())
            return Response.fail("USER NOT FOUND");
        User user = optionalUser.get();
        user.setActive(false);
        repository.save(user);
        return Response.ok();
    }

    public Response<UserResponseDto> getMe(String userId) {
        Optional<User> optionalUser = repository.findByKeycloakId(userId);
        return optionalUser.map(user -> Response.okData(mapper.toUserResponseDto(user))).orElseGet(() -> Response.fail("USER NOT FOUND"));
    }

    public Response<Object> updateMe(String userId, UserUpdateDto dto) {
        Optional<User> optionalUser = repository.findByKeycloakId(userId);
        if(optionalUser.isEmpty())
            return Response.fail("USER NOT FOUND");
        User user = optionalUser.get();
        user.setPhone(dto.getPhone());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        repository.save(user);
        return Response.ok();
    }

    public Response<Object> deactivateMe(String userId) {
        Optional<User> optionalUser = repository.findByKeycloakId(userId);
        if(optionalUser.isEmpty())
            return Response.fail("USER NOT FOUND");
        User user = optionalUser.get();
        user.setActive(false);
        repository.save(user);
        return Response.ok();
    }
}
