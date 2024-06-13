package dev.peytob.mmo.backend.configuration.security

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(title = "MMO backend API", version = "v1"),
    security = [SecurityRequirement(name = "security_auth")]
)
@SecurityScheme(
    name = "security_auth",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER,
    paramName = AUTHORIZATION_HEADER
)
class SwaggerConfiguration {

    @Bean
    fun restApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("api")
        .displayName("MMO Backend API")
        .packagesToScan("dev.peytob.mmo.backend.controller")
        .build()
}
