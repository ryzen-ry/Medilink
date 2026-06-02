package com.proyecto.medilink.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI mediLinkOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Medilink API")
                .description("API de gestión de citas y servicios médicos")
                .version("1.0.0")
                .contact(new Contact()
                    .name("Medilink Support")
                    .email("support@medilink.com")
                    .url("https://medilink.com"))
                .license(new License()
                    .name("Apache 2.0")
                    .url("https://www.apache.org/licenses/LICENSE-2.0.html")));
    }
}
