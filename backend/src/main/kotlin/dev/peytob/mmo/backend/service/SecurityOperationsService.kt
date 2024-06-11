package dev.peytob.mmo.backend.service

interface SecurityOperationsService {

    fun makeSecuredHash(data: String): ByteArray

    fun checkSecuredHash(data: String, dataHash: ByteArray): Boolean
}
