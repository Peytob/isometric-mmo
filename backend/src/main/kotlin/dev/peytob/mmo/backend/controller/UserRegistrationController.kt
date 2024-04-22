package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
private class UserRegistrationController(
    private val userManagementService: UserManagementService
) {

    @PostMapping("/register")
    @Operation(
        summary = "Регистрация авторизованного в Keycloak пользователя в бекенде ММО"
    )
    fun registerNewUser(authorization: Authentication) {
        val externalUserId = extractExternalUserId(authorization)
        userManagementService.registerUser(externalUserId)
    }

    private fun extractExternalUserId(authorization: Authentication) = authorization.name
}
