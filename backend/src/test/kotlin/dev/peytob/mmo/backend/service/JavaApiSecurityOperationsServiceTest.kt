package dev.peytob.mmo.backend.service

class JavaApiSecurityOperationsServiceTest : SecurityOperationsServiceTest() {

    override fun getInstance(): SecurityOperationsService {
        return JavaApiSecurityOperationsService()
    }
}