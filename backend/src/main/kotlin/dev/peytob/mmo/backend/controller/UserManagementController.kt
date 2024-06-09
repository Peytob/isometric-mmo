package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.RegistrationDto
import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/user")
internal class UserManagementController(
    private val userManagementService: UserManagementService,
    private val userMapper: UserMapper
) {

    @PostMapping("/register")
    @Operation(
        summary = "Регистрация нового пользователя в бекенде ММО"
    )
    fun register(@RequestBody registrationDto: RegistrationDto): UserInfoResponse {
        val userRegistrationData = userMapper.fromControllerDtoToServiceDto(registrationDto)
        val user = userManagementService.registerUser(userRegistrationData)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }

//    @PostMapping("/login")
//    @Operation(
//        summary = "Авторизация пользователя в бекенде ММО"
//    )
//    fun login(authorization: Authentication): UserInfoResponse {
//    }
//
//    @PostMapping("/logout")
//    @Operation(
//        summary = "Окончание текущей сессии в бекенде ММО"
//    )
//    fun logout(authorization: Authentication): UserInfoResponse {
//    }
//
//    @PostMapping("/check")
//    @Operation(
//        summary = "Проверка корректности в бекенде ММО"
//    )
//    fun checkAuthorization(authorization: Authentication): UserInfoResponse {
//    }

    @GetMapping("/me")
    @Operation(
        summary = "Получение информации о текущем авторизованном пользователе"
    )
    fun getMe(authorization: Authentication): UserInfoResponse {
        val userId = extractUserId(authorization)
        val user = userManagementService.findUserData(userId) ?: throw ResponseStatusException(NOT_FOUND)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }

    private fun extractUserId(authorization: Authentication) = authorization.name
}
