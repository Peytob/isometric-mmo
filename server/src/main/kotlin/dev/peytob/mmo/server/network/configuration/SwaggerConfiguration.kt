package dev.peytob.mmo.server.network.configuration

import dev.peytob.mmo.core.network.BACKEND_AUTHORIZATION_HEADER
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
    info = Info(title = "MMO server API", version = "v1"),
    security = [SecurityRequirement(name = "security_auth")]
)
@SecurityScheme(
    name = "security_auth",
    type = SecuritySchemeType.APIKEY,
    `in` = SecuritySchemeIn.HEADER,
    paramName = BACKEND_AUTHORIZATION_HEADER
)
class SwaggerConfiguration {

    @Bean
    fun httpApi(): GroupedOpenApi = GroupedOpenApi.builder()
        .group("api")
        .displayName("MMO server API")
        .packagesToScan("dev.peytob.mmo.server.network.http.controller")
        .build()
}
