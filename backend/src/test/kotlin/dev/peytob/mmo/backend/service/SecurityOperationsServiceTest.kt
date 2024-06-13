package dev.peytob.mmo.backend.service

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

abstract class SecurityOperationsServiceTest {

    private lateinit var securityOperationsService: SecurityOperationsService

    abstract fun getInstance(): SecurityOperationsService

    @BeforeEach
    fun setUp() {
        securityOperationsService = getInstance()
    }

    @Test
    fun createdHashIsAlwaysSame() {
        val givenDataString = "Hash me!"

        assertDoesNotThrow {
            securityOperationsService.makeSecuredHash(givenDataString)
        }
    }

    @Test
    fun canCompareHashAndOriginalData() {
        val givenDataString = "Hash me!"
        val givenDataHash = securityOperationsService.makeSecuredHash(givenDataString)

        assertTrue(securityOperationsService.checkSecuredHash(givenDataString, givenDataHash))
    }
}