package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
@Tag(
    name = "User management",
    description = "Пользовательское управление своей учетной записью внутри сервиса"
)
class UserManagementController(
    private val userManagementService: UserManagementService,
    private val userMapper: UserMapper
) {

    @PostMapping("/register")
    @Operation(
        summary = "Регистрация авторизованного в Keycloak пользователя в бекенде ММО"
    )
    fun registerNewUser(authorization: Authentication): UserInfoResponse {
        val externalUserId = authorization.name
        val user = userManagementService.registerUser(externalUserId)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }
}
