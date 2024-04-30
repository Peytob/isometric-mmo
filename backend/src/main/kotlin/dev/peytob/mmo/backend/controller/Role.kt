package dev.peytob.mmo.backend.controller

const val ADMIN_ROLE = "ROLE_ADMIN"

enum class Role(
    val securityString: String
) {

    ADMIN(ADMIN_ROLE);
}
