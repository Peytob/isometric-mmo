package dev.peytob.mmo.backend.controller.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class RegistrationDto(
    @field:NotBlank
    val username: String?,

    @field:NotBlank
    @field:Size(min = 6)
    val password: String?
)
