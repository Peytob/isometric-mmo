package dev.peytob.mmo.backend.configuration.security

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component

@Component
class JwtConverter(
    private val wrappedConverter: JwtGrantedAuthoritiesConverter
) : Converter<Jwt, AbstractAuthenticationToken> {

    override fun convert(source: Jwt): AbstractAuthenticationToken {
        val grantedAuthorities = wrappedConverter.convert(source)
        return JwtAuthenticationToken(source, grantedAuthorities)
    }
}
