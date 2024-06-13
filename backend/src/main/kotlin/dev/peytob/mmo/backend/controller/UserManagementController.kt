package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/user")
internal class UserManagementController(
    private val userManagementService: UserManagementService,
    private val userMapper: UserMapper
) {

    @GetMapping("/me")
    @Operation(
        summary = "Получение информации о текущем авторизованном пользователе"
    )
    fun getMe(authentication: Authentication): UserInfoResponse {
        val token = (authentication.principal as UserDetails).password
        val user = userManagementService.findUserByUsername(token)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }
}
