package dev.peytob.mmo.backend.service.dto

import java.time.Instant
import java.util.UUID

interface UserSession {
    val userId: UUID
    val token: String
    val expiresAt: Instant
    val createdAt: Instant
}
