package dev.peytob.mmo.server.network.configuration.security

import dev.peytob.mmo.core.network.BACKEND_AUTHORIZATION_HEADER
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter

@Configuration
class SecurityConfiguration {

    @Bean
    fun mmoBaseFilterChain(http: HttpSecurity, requestHeaderAuthenticationFilter: RequestHeaderAuthenticationFilter): SecurityFilterChain =
        http
            .cors(CorsConfigurer<HttpSecurity>::disable)
            .csrf(CsrfConfigurer<HttpSecurity>::disable)
            .addFilter(requestHeaderAuthenticationFilter)
            .authorizeHttpRequests { authorizeHttpRequests ->
                authorizeHttpRequests
                    .requestMatchers("/details").permitAll()
                    .requestMatchers("/actuator/**").permitAll()
                    .requestMatchers("/v3/api-docs/**", "/swagger-ui.html", "/swagger-ui/**").permitAll()
                    .anyRequest().authenticated()
            }
            .build()

    @Bean
    fun authenticationManager(
        preAuthenticatedAuthenticationProvider: PreAuthenticatedAuthenticationProvider
    ): AuthenticationManager {
        val providerManager = ProviderManager(preAuthenticatedAuthenticationProvider)
        providerManager.isEraseCredentialsAfterAuthentication = false
        return providerManager
    }

    @Bean
    fun preAuthenticatedAuthenticationProvider(userDetailsService: UserDetailsService): PreAuthenticatedAuthenticationProvider {
        val preAuthenticatedAuthenticationProvider = PreAuthenticatedAuthenticationProvider()
        preAuthenticatedAuthenticationProvider.setPreAuthenticatedUserDetailsService(UserDetailsByNameServiceWrapper(userDetailsService))
        return preAuthenticatedAuthenticationProvider
    }

    @Bean
    fun requestHeaderAuthenticationFilter(authenticationManager: AuthenticationManager): RequestHeaderAuthenticationFilter {
        val requestHeaderAuthenticationFilter = RequestHeaderAuthenticationFilter()
        requestHeaderAuthenticationFilter.setPrincipalRequestHeader(BACKEND_AUTHORIZATION_HEADER)
        requestHeaderAuthenticationFilter.setAuthenticationManager(authenticationManager)
        requestHeaderAuthenticationFilter.setExceptionIfHeaderMissing(false)
        return requestHeaderAuthenticationFilter
    }
}
