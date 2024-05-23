package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.Character
import dev.peytob.mmo.backend.service.dto.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

private val log = LoggerFactory.getLogger(CharacterManagementServiceImpl::class.java)

@Service
private class CharacterManagementServiceImpl(
    private val characterCrudService: CharacterCrudService
) : CharacterManagementService {

    override fun createUserCharacter(user: User, characterName: String): Character {
        log.info("Creating new character with name {} for user {}", characterName, user.id)
        return characterCrudService.createCharacter(user, characterName)
    }

    override fun getUserCharacters(user: User): Collection<Character> =
        characterCrudService.getUserCharacters(user)
}