package dev.peytob.mmo.backend.service.dto

import java.time.Instant
import java.util.*

data class User(
    val id: UUID,
    val username: String,
    val registrationTimestamp: Instant
)
