package dev.peytob.mmo.backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.UuidGenerator
import java.util.*

@MappedSuperclass
abstract class AbstractEntity(

    @get:Id
    @get:GeneratedValue
    @get:UuidGenerator(style = UuidGenerator.Style.TIME)
    @get:Column(name = "ID", nullable = false)
    open var id: UUID? = null
)
