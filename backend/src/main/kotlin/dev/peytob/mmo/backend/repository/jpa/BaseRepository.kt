package dev.peytob.mmo.backend.repository.jpa

import dev.peytob.mmo.backend.repository.jpa.entity.AbstractEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean
import java.util.UUID

@NoRepositoryBean
interface BaseRepository<T : AbstractEntity> : JpaRepository<T, UUID>
