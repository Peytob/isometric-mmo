package dev.peytob.mmo.backend.service.dto

import java.time.Instant

data class User(
    val id: String,
    val externalId: String,
    val registrationTimestamp: Instant
)
