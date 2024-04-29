package dev.peytob.mmo.backend.controller.dto

import java.time.Instant
import java.util.UUID

data class UserInfoResponse(
    val id: UUID,
    val externalId: String,
    val registrationTimestamp: Instant
)
