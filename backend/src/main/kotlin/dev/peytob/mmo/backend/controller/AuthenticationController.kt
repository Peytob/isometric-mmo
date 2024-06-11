package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.LoginDto
import dev.peytob.mmo.backend.controller.dto.RegistrationDto
import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val userManagementService: UserManagementService,
    private val authenticationManager: AuthenticationManager,
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

    @PostMapping("/login")
    @Operation(
        summary = "Авторизация пользователя в бекенде ММО"
    )
    fun login(@RequestBody loginDto: LoginDto): ResponseEntity<*> {
        val authenticationRequest = unauthenticated(loginDto.username, loginDto.password)
        val authentication = authenticationManager.authenticate(authenticationRequest)

        return ResponseEntity.accepted()
            .header("Authentication", authentication.principal.toString())
            .build<Any>()
    }
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
}