package dev.peytob.mmo.backend.repository.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.time.Instant

@Entity(name = "User")
@Table(name = "USERS")
open class UserEntity(

    @get:Column(name = "USERNAME", nullable = false, unique = true)
    open var username: String? = null,

    @get:Column(name = "USERNAME", nullable = false, unique = true)
    open var passwordHash: ByteArray? = null,

    @get:Column(name = "REGISTRATION_DATE", nullable = false, updatable = false)
    @get:CreationTimestamp
    open var registrationTimestamp: Instant? = null

): AbstractEntity()
