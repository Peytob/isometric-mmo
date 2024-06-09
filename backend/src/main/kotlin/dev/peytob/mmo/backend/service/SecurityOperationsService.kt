package dev.peytob.mmo.backend.service

interface SecurityOperationsService {

    fun makeSecuredHash(password: String): ByteArray
}
