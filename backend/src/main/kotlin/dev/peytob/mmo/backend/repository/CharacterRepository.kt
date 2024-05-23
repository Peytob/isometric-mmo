package dev.peytob.mmo.backend.repository

import dev.peytob.mmo.backend.repository.entity.CharacterEntity
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface CharacterRepository : BaseRepository<CharacterEntity> {

    fun existsByName(characterName: String): Boolean

    fun findAllByUserId(userId: UUID): Collection<CharacterEntity>
}
