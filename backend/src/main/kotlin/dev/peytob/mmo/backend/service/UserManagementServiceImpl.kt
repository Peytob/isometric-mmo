package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.dto.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val log = LoggerFactory.getLogger(UserManagementServiceImpl::class.java)

@Service
private class UserManagementServiceImpl(
    private val userCrudService: UserCrudService
) : UserManagementService {

    @Transactional
    override fun registerUser(externalUserId: String): User {
        log.info("Registration new user with external id {}", externalUserId)
        return userCrudService.createUser(externalUserId)
    }

    override fun findUserData(externalUserId: String): User? = userCrudService.findUserByExternalId(externalUserId)
}
