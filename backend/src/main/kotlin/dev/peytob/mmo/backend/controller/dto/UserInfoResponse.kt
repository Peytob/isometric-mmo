package dev.peytob.mmo.backend.controller.dto

import java.time.Instant

data class UserInfoResponse(
    val id: String,
    val externalId: String,
    val registrationTimestamp: Instant
)
