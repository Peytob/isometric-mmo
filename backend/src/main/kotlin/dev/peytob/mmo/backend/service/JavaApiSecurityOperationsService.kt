package dev.peytob.mmo.backend.service

import org.bouncycastle.util.Arrays
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom

@Service
private class JavaApiSecurityOperationsService : SecurityOperationsService {

    private val digest = MessageDigest.getInstance("SHA-256")

    private val random = SecureRandom()

    override fun makeSecuredHash(password: String): ByteArray {
        val passwordBytes = password.encodeToByteArray()
        val saltBytes = ByteArray(digest.digestLength)
        random.nextBytes(saltBytes)
        return makeSecuredHashInternal(passwordBytes, saltBytes)
    }

    private fun makeSecuredHashInternal(data: ByteArray, salt: ByteArray): ByteArray {
        val dataWithSalt = Arrays.concatenate(salt, data)
        return digest.digest(dataWithSalt)
    }
}
