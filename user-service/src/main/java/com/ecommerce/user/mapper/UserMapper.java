package com.ecommerce.user.mapper;

import com.ecommerce.user.dto.UserRequestDto;
import com.ecommerce.user.dto.UserResponseDto;
import com.ecommerce.user.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper{
    User toUser(UserRequestDto userRequestDto);
    UserResponseDto toUserResponseDto(User user);
    List<UserResponseDto> toUserResponseDtoList(List<User> userList);
}
