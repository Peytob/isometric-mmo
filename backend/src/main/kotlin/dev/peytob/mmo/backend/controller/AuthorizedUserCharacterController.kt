package dev.peytob.mmo.backend.controller

import dev.peytob.mmo.backend.controller.dto.CharacterCreateDto
import dev.peytob.mmo.backend.controller.dto.CharacterResponse
import dev.peytob.mmo.backend.mapper.CharacterMapper
import dev.peytob.mmo.backend.service.CharacterManagementService
import dev.peytob.mmo.backend.service.UserManagementService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/me/character")
@Tag(
    name = "Authorized user",
    description = "Управление и доступ к игровым персонажам, связанным с текущей авторизацией в сервисе"
)
class AuthorizedUserCharacterController(
    private val characterManagementService: CharacterManagementService,
    private val userManagementService: UserManagementService,
    private val characterMapper: CharacterMapper
) {

    @PostMapping
    @Operation(
        summary = "Создание персонажа пользователем"
    )
    fun createUserCharacter(authorization: Authentication, @RequestBody characterCreateDto: CharacterCreateDto): CharacterResponse {
        val externalUserId = authorization.name
        val user = userManagementService.findUserData(externalUserId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        val character = characterManagementService.createUserCharacter(user, characterCreateDto.name)
        return characterMapper.fromServiceDtoToControllerDto(character)!!
    }

    @GetMapping
    @Operation(
        summary = "Получение списка персонажей пользователя"
    )
    fun getUserCharacters(authorization: Authentication): Collection<CharacterResponse> {
        val externalUserId = authorization.name
        val user = userManagementService.findUserData(externalUserId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND)
        return characterManagementService
            .getUserCharacters(user)
            .mapNotNull(characterMapper::fromServiceDtoToControllerDto)
    }
}
