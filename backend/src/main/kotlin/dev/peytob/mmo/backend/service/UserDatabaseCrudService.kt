package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.exception.ResourceAlreadyExistsException
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.repository.UserRepository
import dev.peytob.mmo.backend.repository.entity.UserEntity
import dev.peytob.mmo.backend.service.dto.User
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

private val log = LoggerFactory.getLogger(UserDatabaseCrudService::class.java)

@Service
private class UserDatabaseCrudService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserCrudService {

    @Transactional
    override fun createUser(externalUserId: String): User {
        log.info("Creating new user with external id {}", externalUserId)

        if (isUserExistsByExternalId(externalUserId)) {
            throw ResourceAlreadyExistsException("User with external id '$externalUserId' already exists")
        }

        val userEntity = UserEntity(
            externalId = externalUserId,
            registrationTimestamp = Instant.now()
        )

        userRepository.save(userEntity)

        return userMapper.fromHibernateEntityToServiceDto(userEntity)
    }

    override fun isUserExistsByExternalId(externalUserId: String): Boolean = userRepository.existsByExternalId(externalUserId)

    override fun findUserByExternalId(externalUserId: String): User? {
        val userEntity = userRepository.findByExternalId(externalUserId)
        return userMapper.fromHibernateEntityToServiceDto(userEntity)
    }
}
