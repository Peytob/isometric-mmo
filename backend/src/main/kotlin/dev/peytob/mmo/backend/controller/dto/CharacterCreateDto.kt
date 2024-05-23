package dev.peytob.mmo.backend.controller.dto

import jakarta.validation.constraints.NotBlank

data class CharacterCreateDto(
    @field:NotBlank
    val name: String
)
