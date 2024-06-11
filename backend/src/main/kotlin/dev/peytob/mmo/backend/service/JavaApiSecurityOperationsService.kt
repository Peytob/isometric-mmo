package dev.peytob.mmo.backend.service

import org.bouncycastle.util.Arrays
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom

@Service
internal class JavaApiSecurityOperationsService : SecurityOperationsService {

    private val digest = MessageDigest.getInstance("SHA-256")

    private val random = SecureRandom()

    override fun makeSecuredHash(data: String): ByteArray {
        val passwordBytes = data.encodeToByteArray()
        val saltBytes = ByteArray(digest.digestLength)
        random.nextBytes(saltBytes)
        val saltedData = saltData(passwordBytes, saltBytes)
        return saltData(digest.digest(saltedData), saltBytes)
    }

    override fun checkSecuredHash(data: String, dataHash: ByteArray): Boolean {
        val (salt, existsHash) = desaltData(dataHash)
        val saltedData = saltData(data.encodeToByteArray(), salt)
        val newHash = digest.digest(saltedData)
        return newHash.contentEquals(existsHash)
    }

    private fun saltData(data: ByteArray, salt: ByteArray): ByteArray {
        return Arrays.concatenate(salt, data)
    }

    private fun desaltData(saltedData: ByteArray): Pair<ByteArray, ByteArray> {
        val salt = saltedData.sliceArray(0..< digest.digestLength)
        val hash = saltedData.sliceArray(digest.digestLength..< saltedData.size)

        return Pair(salt, hash)
    }
}
