package com.ecommerce.user.component;

import com.ecommerce.user.dto.UserRequestDto;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.mapper.UserMapper;
import com.ecommerce.user.repo.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Value("${spring.sql.init.mode}")
    private String initMode;
    @Override
    public void run(String... args) throws Exception {
        if (initMode.equals("always")) {
            UserRequestDto userDto = new UserRequestDto(
                    "user@gmail.com",
                    "user",
                    "user",
                    "Userfirst",
                    "Userlast",
                    "11111111"
            );
            UserRequestDto adminDto = new UserRequestDto(
                    "admin@gmail.com",
                    "admin",
                    "admin",
                    "Adminfirst",
                    "Adminlast",
                    "11111111"
            );
            String userKeycloakId = "374d7815-4cf9-44cc-ab23-4aed52c19d39";
            String adminKeycloakId = "a47520fc-6740-4bee-90ad-28457abc78ea";
            User user = userMapper.toUser(userDto);
            User admin = userMapper.toUser(adminDto);
            user.setKeycloakId(userKeycloakId);
            admin.setKeycloakId(adminKeycloakId);
            userRepository.save(user);
            userRepository.save(admin);
        }
    }
}
