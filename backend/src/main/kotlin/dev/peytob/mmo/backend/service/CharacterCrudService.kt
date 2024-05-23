package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.Character
import dev.peytob.mmo.backend.service.dto.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import java.util.*

interface CharacterCrudService {

    fun createCharacter(user: User, characterName: String): Character

    fun isCharacterExistsByName(characterName: String): Boolean

    fun getCharactersPage(pageable: Pageable): Page<Character>

    fun findCharacterById(characterId: UUID): Character?

    fun getUserCharacters(user: User): Collection<Character>
}
