package dev.peytob.mmo.backend.configuration.security

import dev.peytob.mmo.backend.controller.Role
import dev.peytob.mmo.backend.service.UserCrudService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher


@Configuration
class SecurityConfiguration(
    private val userCrudService: UserCrudService
) {

    @Bean
    fun mmoBaseFilterChain(http: HttpSecurity, requestHeaderAuthenticationFilter: RequestHeaderAuthenticationFilter): SecurityFilterChain =
        http
            .cors(CorsConfigurer<HttpSecurity>::disable)
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .addFilter(requestHeaderAuthenticationFilter)
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/auth/check").authenticated()
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/admin/**").hasRole(Role.ADMIN.name)
                    .anyRequest().authenticated()
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
    fun authenticationManager(tokenAuthenticationProvider: TokenAuthenticationProvider,
                              preAuthenticatedAuthenticationProvider: PreAuthenticatedAuthenticationProvider
    ): AuthenticationManager {
        val providerManager = ProviderManager(
            tokenAuthenticationProvider,
            preAuthenticatedAuthenticationProvider
        )
        providerManager.isEraseCredentialsAfterAuthentication = false
        return providerManager
    }

    @Bean
    fun preAuthenticatedAuthenticationProvider(userDetailsService: UserDetailsService): PreAuthenticatedAuthenticationProvider {
        val preAuthenticatedAuthenticationProvider = PreAuthenticatedAuthenticationProvider()
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService {
            userDetailsService.loadUserByUsername(it.principal.toString())
        }
        return preAuthenticatedAuthenticationProvider
    }

    @Bean
    fun requestHeaderAuthenticationFilter(authenticationManager: AuthenticationManager): RequestHeaderAuthenticationFilter {
        val requestHeaderAuthenticationFilter = RequestHeaderAuthenticationFilter()
        requestHeaderAuthenticationFilter.setPrincipalRequestHeader(AUTHORIZATION_HEADER)
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager)
        requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false)
        return requestHeaderAuthenticationFilter
    }
}
