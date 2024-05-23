package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserCrudService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
@RestController
@RequestMapping("/admin/user")
@Tag(
    name = "Admin user access",
    description = "Получение данных о зарегистрированных в сервисе пользователях администратором системы"
)
class UserAccessController(
    private val userCrudService: UserCrudService,
    private val userMapper: UserMapper
) {

    @GetMapping
    @Operation(
        summary = "Получение страницы со списком зарегистрированных пользователей"
    )
    fun getUsersPage(@ParameterObject @PageableDefault pageable: Pageable): Collection<UserInfoResponse> {
        return userCrudService
            .getUsersPage(pageable)
            .mapNotNull(userMapper::fromServiceDtoToControllerDto)
    }

    @GetMapping("/id/{id}")
    @Operation(
        summary = "Получение конкретного пользователя по его ID"
    )
    fun getUserById(@PathVariable userId: UUID): UserInfoResponse {
        val user = userCrudService.findUserById(userId)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }

    @GetMapping("/externalId/{externalId}")
    @Operation(
        summary = "Получение конкретного пользователя по его ExternalID"
    )
    fun getUserByExternalId(@PathVariable externalUserId: String): UserInfoResponse {
        val user = userCrudService.findUserByExternalId(externalUserId)
        return userMapper.fromServiceDtoToControllerDto(user)!!
    }
}

