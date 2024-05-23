package dev.peytob.mmo.backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity(name = "Character")
@Table(name = "CHARACTERS")
open class CharacterEntity(

    @get:Column(name = "NAME", nullable = false, unique = true)
    open var name: String? = null,

    @get:ManyToOne(optional = false)
    @get:JoinColumn(name = "USER_ID")
    open var user: UserEntity? = null,

): AbstractEntity()
