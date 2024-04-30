package dev.peytob.mmo.backend.configuration.security

import dev.peytob.mmo.backend.configuration.security.filter.RegisteredOnlyUsersFilter
import dev.peytob.mmo.backend.controller.Role
import dev.peytob.mmo.backend.service.UserCrudService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.intercept.AuthorizationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher

@Configuration
//@EnableMethodSecurity(securedEnabled = true)
class SecurityConfiguration(
    private val userCrudService: UserCrudService
) {

    @Bean
    fun mmoBaseFilterChain(http: HttpSecurity, jwtConverter: JwtConverter): SecurityFilterChain =
        http
            .cors(CorsConfigurer<HttpSecurity>::disable)
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .addFilterAfter(RegisteredOnlyUsersFilter(userCrudService), AuthorizationFilter::class.java)
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/admin/**").hasRole(Role.ADMIN.name)
                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtConverter)
                }
            }
            .build()

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer =
        WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring().requestMatchers(
                antMatcher("/v3/api-docs/**"),
                antMatcher("/swagger-ui.html"),
                antMatcher("/swagger-ui/**")
            )
        }

    @Bean
    fun jwtGrantedAuthoritiesConverter(): JwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
}
