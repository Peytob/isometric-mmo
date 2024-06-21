package dev.peytob.mmo.server.network.service

import dev.peytob.mmo.server.network.model.backend.BackendUser

interface UserAccessService {

    fun findBackendUserById(userId: String): BackendUser?

    fun findBackendUserByToken(token: String): BackendUser?
}
