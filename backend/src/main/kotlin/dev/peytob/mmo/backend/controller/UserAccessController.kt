package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.service.UserCrudService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
@RequestMapping("/admin/user")
class UserAccessController(
    private val userCrudService: UserCrudService,
    private val userMapper: UserMapper
) {

    @GetMapping("/")
    fun getUsersPage(@ParameterObject @PageableDefault pageable: Pageable): Page<UserInfoResponse> {
        return userCrudService
            .getUsersPage(pageable)
            .map(userMapper::fromServiceDtoToControllerDto)
    }

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable userId: UUID): UserInfoResponse {
        val user = userCrudService.findUserById(userId)
            ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "User with given id not found")

        return userMapper.fromServiceDtoToControllerDto(user)!!
    }
}
