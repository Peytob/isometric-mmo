package dev.peytob.mmo.backend.controller.dto

import java.time.Instant
import java.util.*

data class UserSessionResponse(
    val userId: UUID,
    val expiresAt: Instant,
    val createdAt: Instant
)