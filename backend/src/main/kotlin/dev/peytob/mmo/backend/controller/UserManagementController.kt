package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/user")
private class UserManagementController(
    private val userManagementService: UserManagementService,
    private val userMapper: UserMapper
) {

    @PostMapping("/register")
    @Operation(
        summary = "Регистрация авторизованного в Keycloak пользователя в бекенде ММО"
    )
    fun registerNewUser(authorization: Authentication): UserInfoResponse {
        val externalUserId = extractExternalUserId(authorization)
        val user = userManagementService.registerUser(externalUserId)
        return userMapper.fromServiceDtoToControllerDto(user)
    }

    @GetMapping("/me")
    @Operation(
        summary = "Получение информации о текущем авторизованном пользователе"
    )
    fun getMe(authorization: Authentication): UserInfoResponse {
        val externalUserId = extractExternalUserId(authorization)
        val user = userManagementService.findUserData(externalUserId) ?: throw ResponseStatusException(NOT_FOUND)
        return userMapper.fromServiceDtoToControllerDto(user)
    }

    private fun extractExternalUserId(authorization: Authentication) = authorization.name
}
