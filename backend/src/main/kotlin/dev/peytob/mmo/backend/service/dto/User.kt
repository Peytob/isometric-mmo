package dev.peytob.mmo.backend.service.dto

import java.time.Instant
import java.util.*

data class User(
    val id: UUID,
    val externalId: String,
    val registrationTimestamp: Instant
)
