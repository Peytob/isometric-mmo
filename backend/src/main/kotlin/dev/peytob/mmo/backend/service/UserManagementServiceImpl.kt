package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.service.UserManagementService.UserRegistrationData
import dev.peytob.mmo.backend.service.dto.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

private val log = LoggerFactory.getLogger(UserManagementServiceImpl::class.java)

@Service
private class UserManagementServiceImpl(
    private val userCrudService: UserCrudService,
    private val securityOperationsService: SecurityOperationsService
) : UserManagementService {

    @Transactional
    override fun registerUser(userRegistrationData: UserRegistrationData): User {
        log.info("Registration new user with username {}", userRegistrationData.username)
        val passwordHash = securityOperationsService.makeSecuredHash(userRegistrationData.password)
        return userCrudService.createUser(userRegistrationData.username, passwordHash)
    }

    override fun findUserByUserId(userId: UUID): User? = userCrudService.findUserById(userId)

    override fun findUserByUsername(username: String): User? = userCrudService.findUserByUsername(username)

    override fun findUserByCredentials(username: String, password: String): User? {
        val user = findUserByUsername(username) ?: return null
        val isPasswordCorrect = securityOperationsService.checkSecuredHash(password, user.passwordHash)
        return if (isPasswordCorrect) user else null
    }
}
