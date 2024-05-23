package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.Character
import dev.peytob.mmo.backend.service.dto.User

interface CharacterManagementService {

    fun createUserCharacter(user: User, characterName: String): Character

    fun getUserCharacters(user: User): Collection<Character>
}
