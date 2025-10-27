package com.ecommerce.notification.dto;

import com.ecommerce.notification.enums.Channel;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendRequest {
    @NotBlank
    private String userId;
    @NotBlank
    private String title;
    @NotBlank
    private String body;

    private Channel channel;
}
