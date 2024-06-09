package dev.peytob.mmo.backend.service

import dev.peytob.mmo.backend.exception.ResourceAlreadyExistsException
import dev.peytob.mmo.backend.mapper.UserMapper
import dev.peytob.mmo.backend.repository.UserRepository
import dev.peytob.mmo.backend.repository.entity.UserEntity
import dev.peytob.mmo.backend.service.dto.User
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.*

private val log = LoggerFactory.getLogger(UserDatabaseCrudService::class.java)

@Service
private class UserDatabaseCrudService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper
) : UserCrudService {

    @Transactional
    override fun createUser(username: String, passwordHash: ByteArray): User {
        log.info("Creating new user with username {}", username)

        if (isUserExistsByUsername(username)) {
            throw ResourceAlreadyExistsException("User with username '$username' already exists")
        }

        val userEntity = UserEntity(
            passwordHash = passwordHash,
            registrationTimestamp = Instant.now()
        )

        userRepository.save(userEntity)

        return userMapper.fromHibernateEntityToServiceDto(userEntity)!!
    }

    override fun isUserExistsByUsername(userId: String): Boolean = userRepository.isUserExistsById(userId)

    override fun findUserById(userId: String): User? {
        val userEntity = userRepository.findUserById(userId)
        return userMapper.fromHibernateEntityToServiceDto(userEntity)
    }

    override fun getUsersPage(pageable: Pageable): Page<User> {
        return userRepository
            .findAll(pageable)
            .map(userMapper::fromHibernateEntityToServiceDto)
    }

    override fun findUserById(userId: UUID): User? {
        val user = userRepository.findByIdOrNull(userId)
        return userMapper.fromHibernateEntityToServiceDto(user)
    }
}
