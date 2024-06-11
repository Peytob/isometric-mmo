package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import dev.peytob.mmo.backend.service.UserSessionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/user")
internal class UserManagementController(
    private val userManagementService: UserManagementService,
    private val userSessionService: UserSessionService,
    private val userMapper: UserMapper
) {

    @GetMapping("/me")
    @Operation(
        summary = "Получение информации о текущем авторизованном пользователе"
    )
    fun getMe(authorization: Authentication): UserInfoResponse {
        val userId = extractUserId(authorization)
        val user = userManagementService.findUserByUserId(userId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }

    private fun extractUserId(authorization: Authentication): UUID {
        return UUID.fromString(authorization.name)
    }
}
