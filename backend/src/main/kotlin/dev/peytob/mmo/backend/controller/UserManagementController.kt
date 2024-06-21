package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserManagementService
import dev.peytob.mmo.backend.service.UserSessionService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
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
    fun getMe(authentication: Authentication): ResponseEntity<*> {
        val token = (authentication.principal as UserDetails).password
        val userSession = userSessionService.getTokenActiveSession(token)
            ?: return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build<Any>()
        val user = userManagementService.findUserByUserId(userSession.userId)!!
        val userInfoResponse = userMapper.fromServiceDtoToControllerDto(user)!!
        return ResponseEntity.ok(userInfoResponse)
    }
}
