package com.ecommerce.order.client.fallback;

import com.ecommerce.order.client.UserServiceClient;
import org.springframework.stereotype.Component;

@Component
public class UserServiceFallback implements UserServiceClient {
    @Override
    public String hello() {
        return "Service is not available";
    }
}
