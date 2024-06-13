package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.configuration.security.AUTHORIZATION_HEADER
import dev.peytob.mmo.backend.controller.dto.LoginDto
import dev.peytob.mmo.backend.controller.dto.RegistrationDto
import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import dev.peytob.mmo.backend.service.UserSessionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken.unauthenticated
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthenticationController(
    private val userManagementService: UserManagementService,
    private val authenticationManager: AuthenticationManager,
    private val userSessionService: UserSessionService,
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
            .header(AUTHORIZATION_HEADER, authentication.principal.toString())
            .build<Any>()
    }

    @PostMapping("/logout")
    @Operation(
        summary = "Окончание текущей сессии в бекенде ММО"
    )
    fun logout(authentication: Authentication): ResponseEntity<*> {
        val token = (authentication.principal as UserDetails).password

        val userSession = userSessionService.getTokenActiveSession(token)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<Any>()

        userSessionService.endUserActiveSession(userSession)

        return ResponseEntity.accepted().build<Any>()
    }

    @PostMapping("/check")
    @Operation(
        summary = "Проверка свежести токена в бекенде ММО"
    )
    fun checkAuthorization() {
    }
}