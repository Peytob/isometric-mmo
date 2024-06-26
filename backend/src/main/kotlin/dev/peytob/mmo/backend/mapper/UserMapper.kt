package dev.peytob.mmo.backend.mapper

import dev.peytob.mmo.backend.controller.dto.RegistrationDto
import dev.peytob.mmo.backend.controller.dto.UserInfoResponse
import dev.peytob.mmo.backend.repository.jpa.entity.UserEntity
import dev.peytob.mmo.backend.service.UserManagementService.UserRegistrationData
import dev.peytob.mmo.backend.service.dto.User
import org.mapstruct.InjectionStrategy
import org.mapstruct.Mapper
import org.mapstruct.MappingConstants.ComponentModel

@Mapper(
    componentModel = ComponentModel.SPRING,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
interface UserMapper {

    fun fromHibernateEntityToServiceDto(userEntity: UserEntity?): User?

    fun fromServiceDtoToControllerDto(user: User?): UserInfoResponse?

    fun fromControllerDtoToServiceDto(registrationDto: RegistrationDto): UserRegistrationData
}
