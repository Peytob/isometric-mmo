package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.exception.ResourceAlreadyExistsException
import dev.peytob.mmo.backend.mapper.CharacterMapper
import dev.peytob.mmo.backend.repository.CharacterRepository
import dev.peytob.mmo.backend.repository.entity.CharacterEntity
import dev.peytob.mmo.backend.repository.entity.UserEntity
import dev.peytob.mmo.backend.service.dto.Character
import dev.peytob.mmo.backend.service.dto.User
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private val log = LoggerFactory.getLogger(CharacterDatabaseCrudService::class.java)

@Service
private class CharacterDatabaseCrudService(
    private val characterRepository: CharacterRepository,
    private val characterMapper: CharacterMapper
) : CharacterCrudService {

    @Transactional
    override fun createCharacter(user: User, characterName: String): Character {
        log.info("Creating new database character record for character {} for user {}", characterName, user.id)

        if (isCharacterExistsByName(characterName)) {
            log.error("Character with name {} already exists", characterName)
            throw ResourceAlreadyExistsException("Character with name '$characterName' already exists")
        }

        val characterEntity = CharacterEntity(
            name = characterName,
            user = UserEntity().also { it.id = user.id }
        )

        characterRepository.save(characterEntity)

        return characterMapper.fromHibernateEntityToServiceDto(characterEntity)!!
    }

    @Transactional(readOnly = true)
    override fun isCharacterExistsByName(characterName: String): Boolean =
        characterRepository.existsByName(characterName)

    @Transactional(readOnly = true)
    override fun getCharactersPage(pageable: Pageable): Page<Character> {
        return characterRepository
            .findAll(pageable)
            .map(characterMapper::fromHibernateEntityToServiceDto)
    }

    @Transactional(readOnly = true)
    override fun findCharacterById(characterId: UUID): Character? {
        val character = characterRepository.findByIdOrNull(characterId)
        return characterMapper.fromHibernateEntityToServiceDto(character)
    }

    @Transactional(readOnly = true)
    override fun getUserCharacters(user: User): Collection<Character> {
        return characterRepository
            .findAllByUserId(user.id)
            .mapNotNull(characterMapper::fromHibernateEntityToServiceDto)
    }
}