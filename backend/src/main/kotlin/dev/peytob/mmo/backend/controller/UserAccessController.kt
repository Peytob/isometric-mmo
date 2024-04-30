package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.service.UserCrudService
import org.springdoc.core.annotations.ParameterObject
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/admin/user")
class UserAccessController(
    private val userCrudService: UserCrudService
) {

    @GetMapping("/")
    fun getUsersPage(@ParameterObject @PageableDefault pageable: Pageable) = userCrudService.getUsersPage(pageable)

    @GetMapping("/id/{id}")
    fun getUserById(@PathVariable userId: UUID) = userCrudService.findUserById(userId)
}
