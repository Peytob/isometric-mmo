package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/me")
@Tag(
    name = "Authorized user",
    description = "Получение данных о текущей авторизации в сервисе"
)
class AuthorizedUserController(
    private val userManagementService: UserManagementService,
    private val userMapper: UserMapper
) {

    @GetMapping
    @Operation(
        summary = "Получение информации о текущем авторизованном пользователе"
    )
    fun getMe(authorization: Authentication): UserInfoResponse {
        val externalUserId = authorization.name
        val user = userManagementService.findUserData(externalUserId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }
}
