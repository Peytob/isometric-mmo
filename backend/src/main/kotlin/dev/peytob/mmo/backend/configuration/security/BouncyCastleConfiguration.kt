package dev.peytob.mmo.backend.configuration.security

import jakarta.annotation.PostConstruct
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import java.security.Security

private val log = LoggerFactory.getLogger(BouncyCastleConfiguration::class.java)

@Configuration
class BouncyCastleConfiguration {

    @PostConstruct
    fun setUpBouncyCastleProvider() {
        log.info("Adding bouncy castle java API security provider")
        Security.addProvider(BouncyCastleProvider())
    }
}