package dev.peytob.mmo.backend.service.dto

import java.time.Instant

data class User(
    val externalId: String,
    val registrationTimestamp: Instant
)
