package com.ecommerce.payment.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        final String OAUTH_SCHEME = "oauth2";
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(OAUTH_SCHEME,
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .flows(new OAuthFlows()
                                                .authorizationCode(new OAuthFlow()
                                                        .authorizationUrl("http://localhost:8443/realms/ecommerce/protocol/openid-connect/auth")
                                                        .tokenUrl("http://localhost:8443/realms/ecommerce/protocol/openid-connect/token")
                                                        .scopes(new Scopes()
                                                                .addString("openid","OpenID scope")
                                                                .addString("profile","User profile")
                                                                .addString("email","User email")
                                                        )
                                                )
                                        )
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList(OAUTH_SCHEME));
    }
}
